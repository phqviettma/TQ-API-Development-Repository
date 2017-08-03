package com.tq.clickfunnel.lambda.modle;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFPurchase {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("contact")
    private CFContact contact;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updatedAt")
    private Date updated_at;

    @JsonProperty("subscription_id")
    private Integer subscriptionId;

    @JsonProperty("charge_id")
    private Integer chargeId;

    @JsonProperty("products")
    private List<CFProducts> products;

    @Override
    public String toString() {
        return "CFPurchase [id=" + id + ", contact=" + contact + ", status=" + status + ", createdAt=" + createdAt + ", updated_at="
                + updated_at + ", subscriptionId=" + subscriptionId + ", chargeId=" + chargeId + ", products=" + products + "]";
    }
}
