package io.moneytransfer.api;

import io.moneytransfer.model.Transfer;
import io.moneytransfer.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/transfer")
public class TransferApi {

    @Inject
    private TransferService transferService;

    @POST
    @Consumes({"application/json"})
    @Operation(summary = "moves money", description = "", tags = {"transfer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "transfer successful"),
            @ApiResponse(responseCode = "400", description = "invalid input")})
    public Response transfer(@Parameter(description = "transfer details") Transfer transfer) {
        return transferService.transaction(transfer);
    }
}
