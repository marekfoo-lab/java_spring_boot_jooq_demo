package pl.mfconsulting.java.demo.quarkus_jooq.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import pl.mfconsulting.java.demo.quarkus_jooq.model.AccountDT;
import pl.mfconsulting.java.demo.quarkus_jooq.repository.AccountRepository;

import java.util.List;


@Path("/api/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AccountRepository accountRepository;

    @Inject
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GET
    public List<AccountDT> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getAccountByLogin(@PathParam("id") String login) {
        return accountRepository.findByLogin(login)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @POST
    public Response createAccount(AccountDT newAccount) {
        var createdId = accountRepository.create(newAccount);
        UriBuilder uriBuilder = UriBuilder.fromResource(AccountController.class)
                .path(String.valueOf(createdId));
//        return Response.created(URI.create("/api/accounts/" + createdId)).build();
        return Response.created(uriBuilder.build()).entity(createdId).build();
    }
}
