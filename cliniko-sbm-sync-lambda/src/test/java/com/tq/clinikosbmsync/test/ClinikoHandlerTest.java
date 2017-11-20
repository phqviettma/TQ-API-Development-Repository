package com.tq.clinikosbmsync.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.MockEnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.services.RepositoryServiceImpl;
import com.tq.common.lambda.utils.DynamodbUtils;

public class ClinikoHandlerTest {

	private LambdaContext m_LambdaContext;

	@Before
	public void init() {
		MockEnvVar envVar = new MockEnvVar();
		Map<String, String> env = new HashMap<>();
		env.put(Config.CLINIKO_API_KEY, "edc98dfa5bff69bc2f4cc5d5af5287cf");
		envVar.setValueSystems(env);
		AmazonDynamoDB client = DynamodbUtils.getLocallyDynamoDB();
		m_LambdaContext = LambdaContextImpl.builder().withClient(client).withEnvVar(envVar)
				.withRepositoryService(new RepositoryServiceImpl(client)).build();
	}

	public void testLookUpAppointment() {
		String currentTime = Config.CLINIKO_DATE_FORMAT.format(System.currentTimeMillis());
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setId(2222L);
		LatestClinikoAppts latestAppt = new LatestClinikoAppts();
		//latestAppt.setSynchronized_created(appointmentInfo.getId());
		m_LambdaContext.getClinikoApptService().put(latestAppt);
	}

}
