package ar.com.meli.entrevista.service;

import ar.com.meli.entrevista.controller.CouponController;
import ar.com.meli.entrevista.dto.Item;
import ar.com.meli.entrevista.dto.Node;
import ar.com.meli.entrevista.dto.Response;
import ar.com.meli.entrevista.util.SortByLowerBound;
import ar.com.meli.entrevista.util.SortItem;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CouponService {

    private static final String url = "https://api.mercadolibre.com/items/";
    private static final Gson g = new Gson();
    private static final Logger logger = Logger.getLogger(CouponController.class);

    private static RestTemplate restTemplate = new RestTemplate();
    private static HttpHeaders headers = new HttpHeaders();
    private static HttpEntity<String> entity = new HttpEntity<String>(headers);
    private static ConcurrentHashMap<String,Float> itemAll = new ConcurrentHashMap<>();

    private Float valueMax = 0F;


    @Cacheable("result")
    public Response purchaseOption(Map<String,Float> items, Float amount) throws ResponseStatusException {

        List<String> list = calculate(items,amount);

        if(list == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "item not found"
            );
        } else {
            return new Response(list,valueMax);
        }
    }

    @Cacheable("items")
    public Map<String, Float> findValues(List<String> item_ids) {

        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response;
        LinkedTreeMap<String,Object> aux;
        Float value;

        try {

            Map<String, Float> items = new HashMap<>();

            for(String key:item_ids){

                if(itemAll.containsKey(key)){
                    items.put(key,itemAll.get(key));
                }
                response = restTemplate.exchange(url+key, HttpMethod.GET, entity, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    aux = g.fromJson(response.getBody(),LinkedTreeMap.class);
                    if(aux.get("price")!=null) {
                        value = ((Double)aux.get("price")).floatValue();
                        items.put(key,value);
                        itemAll.put(key,value);
                    }
                }
            }

            return items;

        } catch (Exception e) {
            e.getStackTrace();
            logger.info("error en el servicio");
            logger.error(e.getMessage() + "\n****\n" +e.getCause());
        }

        return null;
    }

    private List<String> calculate(Map<String,Float> items, Float amount) {

        if (items.isEmpty() || amount <= 0) {
            return null;
        }

        return solve(mapToArray(items),amount);
    }

    private Float upperBound(Float tv, Float tw, Integer id, Item arr[], Float amount) {
        Float value = tv;
        Float weight = tw;
        int size = arr.length;
        for (int i = id; i < size; i++) {
            if (weight + arr[i].getValue()
                    <= amount) {
                weight += arr[i].getValue();
                value -= arr[i].getValue();
            }
            else {
                value -= amount - weight;
                break;
            }
        }
        return value;
    }

    private Float lowerBound(Float tv, Float tw, Integer id, Item arr[], Float amount)
    {
        Float value = tv;
        Float weight = tw;
        int size = arr.length;
        for (int i = id; i < size; i++) {
            if (weight + arr[i].getValue()
                    <= amount) {
                weight += arr[i].getValue();
                value -= arr[i].getValue();
            }
            else {
                break;
            }
        }
        return value;
    }

    private void assign(Node a, Float ub, Float lb,
                        Integer level, Boolean flag,
                        Float tv, Float tw) {
        a.setUpperBound(ub);
        a.setLowerBound(lb);
        a.setLevel(level);
        a.setFlag(flag);
        a.setTotalValue(tv);
        a.setTotalWeight(tw);
    }

    private List<String> solve(Item arr[],Float amount)
    {
        List<String> items = new ArrayList<>();
        Arrays.sort(arr, new SortItem());
        int size = arr.length;

        if(arr[size - 1].getValue() > amount){
            return null;
        }


        Node current, left, right;
        current = new Node();
        left = new Node();
        right = new Node();


        float minLB = 0, finalLB
                = Integer.MAX_VALUE;

        current.inicializate();

        PriorityQueue<Node> pq
                = new PriorityQueue<Node>(
                new SortByLowerBound());

        pq.add(current);

        boolean currPath[] = new boolean[size];
        boolean finalPath[] = new boolean[size];

        while (!pq.isEmpty()) {
            current = pq.poll();
            if (current.getUpperBound() > minLB
                    || current.getUpperBound() >= finalLB) {
                continue;
            }

            if (current.getLevel() != 0)
                currPath[current.getLevel() - 1]
                        = current.getFlag();

            if (current.getLevel() == size) {
                if (current.getLowerBound() < finalLB) {
                    for (int i = 0; i < size; i++)
                        finalPath[arr[i].getId()]
                                = currPath[i];
                    finalLB = current.getLowerBound();
                }
                continue;
            }

            int level = current.getLevel();

            assign(right, upperBound(current.getTotalValue(),
                    current.getTotalWeight(),
                    level + 1, arr,amount),
                    lowerBound(current.getTotalValue(), current.getTotalWeight(),
                            level + 1, arr,amount),
                    level + 1, false,
                    current.getTotalValue(), current.getTotalWeight());

            if (current.getTotalWeight() + arr[current.getLevel()].getValue()
                    <= amount) {

                left.setUpperBound(upperBound(
                        current.getTotalValue()
                                - arr[level].getValue(),
                        current.getTotalWeight()
                                + arr[level].getValue(),
                        level + 1, arr,amount));
                left.setLowerBound(lowerBound(
                        current.getTotalValue()
                                - arr[level].getValue(),
                        current.getTotalWeight()
                                + arr[level].getValue(),
                        level + 1,
                        arr,amount));
                assign(left, left.getUpperBound(), left.getLowerBound(),
                        level + 1, true,
                        current.getTotalValue() - arr[level].getValue(),
                        current.getTotalWeight() + arr[level].getValue());
            } else {

                left.setUpperBound(1f);
                left.setLowerBound(1f);
            }

            minLB = Math.min(minLB, left.getLowerBound());
            minLB = Math.min(minLB, right.getLowerBound());

            if (minLB >= left.getUpperBound())
                pq.add(new Node(left));
            if (minLB >= right.getUpperBound())
                pq.add(new Node(right));
        }

        for (int i = 0; i < size; i++) {
            if (finalPath[i])
                items.add(arr[i].getName());
        }

        valueMax = -finalLB;

        return items;
    }

    private Item[] mapToArray(Map<String,Float> itemsMap) {

        int size = itemsMap.size(), i = 0;
        Item arr[] = new Item[size];

        for (Map.Entry<String, Float> entry : itemsMap.entrySet()) {
            arr[i] = new Item(i,entry.getKey(),entry.getValue());
            i++;
        }
        return arr;
    }
}
