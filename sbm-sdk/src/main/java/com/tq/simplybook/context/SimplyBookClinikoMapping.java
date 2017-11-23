package com.tq.simplybook.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.simplybook.resp.ClinikoId;
import com.tq.simplybook.resp.SbmClinikoModel;
import com.tq.simplybook.resp.SimplyBookId;

public class SimplyBookClinikoMapping {

	private static final ObjectMapper mapper = new ObjectMapper();
	private Env env;
	private Map<SimplyBookId, ClinikoId> sbmClinkoMap;
	private Map<ClinikoId, SimplyBookId> clinikoSimplyBookId;

	public SimplyBookClinikoMapping(Env m_env) {
		this.env = m_env;
	}

	public List<String> getAllEnv(Env env) {
		Map<String, String> allEnv = env.getAllEnvVariables();
		Set<String> keySet = allEnv.keySet();
		List<String> result = new ArrayList<String>();
		for (String key : keySet) {
			if (key.startsWith("SBM_CLINIKO_MAPPING_")) {
				result.add(allEnv.get(key));
			}
		}
		return result;
	}

	public List<SbmClinikoModel> parseJson(List<String> result) throws IllegalArgumentException {
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		List<SbmClinikoModel> listSbmId = new ArrayList<>();
		try {
			for (String jsonRes : result) {
				listSbmId.add(mapper.readValue(jsonRes, SbmClinikoModel.class));
			}
			return listSbmId;
		} catch (final Exception e) {
			throw new IllegalArgumentException("Unable to read JSON string", e);
		}

	}

	private Map<SimplyBookId, ClinikoId> putMapSbmCliniko() {
		List<SbmClinikoModel> simplyBookModel = parseJson(getAllEnv(env));
		Map<SimplyBookId, ClinikoId> simplyBookClinikoId = new HashMap<SimplyBookId, ClinikoId>();

		for (int i = 0; i < simplyBookModel.size(); i++) {
			Integer businessId = simplyBookModel.get(i).getBusinessId();
			Integer practitionerId = simplyBookModel.get(i).getPractitionerId();
			String event_id = simplyBookModel.get(i).getEvent_id();
			String unit_id = simplyBookModel.get(i).getUnit_id();
			SimplyBookId simplyBookId = new SimplyBookId(event_id, unit_id);
			ClinikoId clinikoId = new ClinikoId(practitionerId, businessId);
			simplyBookClinikoId.put(simplyBookId, clinikoId);

		}

		return simplyBookClinikoId;
	}

	public ClinikoId sbmClinikoMapping(SimplyBookId simplybookId) {
		ClinikoId clinikoId = new ClinikoId();
		
		if(sbmClinkoMap == null) {
			sbmClinkoMap = putMapSbmCliniko();
		}
		
		clinikoId = sbmClinkoMap.get(simplybookId);
		return clinikoId;
	}

	private Map<ClinikoId, SimplyBookId> putMapCliniko() {
		List<SbmClinikoModel> clinikoModel = parseJson(getAllEnv(env));
		Map<ClinikoId, SimplyBookId> clinikoSimplyBookId = new HashMap<ClinikoId, SimplyBookId>();
		for (int i = 0; i < clinikoModel.size(); i++) {
			Integer businessId = clinikoModel.get(i).getBusinessId();
			Integer practitionerId = clinikoModel.get(i).getPractitionerId();
			String event_id = clinikoModel.get(i).getEvent_id();
			String unit_id = clinikoModel.get(i).getUnit_id();
			SimplyBookId simplyBookId = new SimplyBookId(event_id, unit_id);
			ClinikoId clinikoId = new ClinikoId(practitionerId, businessId);
			clinikoSimplyBookId.put(clinikoId, simplyBookId);

		}
		return clinikoSimplyBookId;
	}

	public SimplyBookId clinikoSbmMapping(ClinikoId clinikoId) {
		if(clinikoSimplyBookId == null) {
			clinikoSimplyBookId = putMapCliniko();
		}
		return clinikoSimplyBookId.get(clinikoId);
	}

}
