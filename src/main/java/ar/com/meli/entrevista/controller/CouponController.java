package ar.com.meli.entrevista.controller;

import javax.validation.Valid;
import ar.com.meli.entrevista.dto.Request;
import ar.com.meli.entrevista.dto.Response;
import ar.com.meli.entrevista.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
@Api(value="Coupon System", description="returns the items you can buy with a coupon")
public class CouponController {

    @Autowired
    CouponService couponService;


    @ApiOperation(value = "return items")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code= 500, message = "Error in server")
    })
    @PostMapping("/coupon")
    public ResponseEntity getItems(
            @ApiParam(value = "returns available items", required = true)
            @Valid @RequestBody Request request) {

        try {
            Map<String,Float> items = couponService.findValues(request.getItem_ids());
            if(items != null && !items.isEmpty()) {
                Response response = couponService.purchaseOption(items,request.getAmount());
                return ResponseEntity.ok(response);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "error getting the price of the item");
            }
        } catch (ResponseStatusException e) {
            return new ResponseEntity(e.getReason(),e.getStatus());
        }
    }
}

