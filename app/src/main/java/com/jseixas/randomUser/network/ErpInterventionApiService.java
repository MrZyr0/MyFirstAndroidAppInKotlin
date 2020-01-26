package com.jseixas.randomUser.network;

import com.jseixas.randomUser.model.RandomUsersResults;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Nicolas Churlet on 21/03/2018.
 */

public interface ErpInterventionApiService {

    // region GETTER ALL
    // region Used
    @GET("?")   // Prepare custom request
    Call<RandomUsersResults> getUsersByCount(
            @Query("results") int nbOfUsers
    );
    // endregion Used
}
