package dev.bestzige.simplemcplugin.api;

import dev.bestzige.simplemcplugin.api.dto.ApiResponse;
import dev.bestzige.simplemcplugin.api.dto.PlayerResponse;
import dev.bestzige.simplemcplugin.api.dto.PlayerSyncRequest;
import dev.bestzige.simplemcplugin.api.dto.TransactionResponse;
import dev.bestzige.simplemcplugin.api.dto.TransferRequest;
import dev.bestzige.simplemcplugin.api.dto.WalletResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.UUID;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

public interface SimpleMcApi {

    @POST("api/players/sync")
    Call<ApiResponse<PlayerResponse>> syncPlayer(
            @Header("Authorization") String authorization,
            @Body PlayerSyncRequest request
    );

    @GET("api/players")
    Call<ApiResponse<PlayerResponse>> getPlayerByUsername(
            @Header("Authorization") String authorization,
            @Query("username") String username
    );

    @GET("api/wallets/{uuid}")
    Call<ApiResponse<WalletResponse>> getWallet(
            @Header("Authorization") String authorization,
            @Path("uuid") UUID uuid
    );

    @POST("api/economy/transfer")
    Call<ApiResponse<TransactionResponse>> transfer(
            @Header("Authorization") String authorization,
            @Body TransferRequest request
    );
}
