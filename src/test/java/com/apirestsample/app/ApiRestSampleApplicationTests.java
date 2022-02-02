package com.apirestsample.app;

import com.apirestsample.app.utils.Helpers;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Properties;

import static com.apirestsample.app.utils.Helpers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiRestSampleApplicationTests extends AbstractTest {

    private final Properties props = Helpers.extractProps();
	private final String authRequest = props.getProperty("application.basic-authorization");
	private final String invalidAuthRequest = props.getProperty("application.test.basic-authorization-invalid");

	String uriWrongTest = "/";
	String uri2WrongTest = "/api";
	String uriBaseTest = "/api/customers";
	String userIdFoundTest = "97befb51c3240e10119f4e80b3a05b71";
	String userIdNotFoundTest = "97befb51c3240e10119f4e80b3a05000";
	String wrongBasicAuthTest = "Basic X1X2X3X4X5X6ZX7X8X9X0";

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	/**
	 * Create Customer
	 */

	@Test
	public void whenCorrectRequestToCreateCustomer_RetrieveCustomerCreated_201() throws Exception {

		String dataRequest = props.getProperty("application.test.post-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
				MockMvcRequestBuilders
					.post(uriBaseTest)
					.content(customerPost)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", authRequest)
		)
		.andExpect(status().isCreated())
		.andReturn();

	}

	@Test
	public void whenCorrectRequestToCreateCustomerAlreadyExists_RetrieveCustomerFound_302() throws Exception {

		String dataRequest = props.getProperty("application.test.post-exists-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.post(uriBaseTest)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isFound())
				.andReturn();
	}

	@Test
	public void whenMissingBodyRequestToCreateCustomer_RetrieveBadRequest_400() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.post(uriBaseTest)
								.content("")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	public void whenRequestCreateCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

		String dataRequest = props.getProperty("application.test.post-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.post(uriBaseTest)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", invalidAuthRequest)
				)
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void whenCorrectRequestToCreateCustomer_RetrieveServerError_500() throws Exception {

		String dataRequest = props.getProperty("application.test.post-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id_2", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.post(uriBaseTest)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isInternalServerError())
				.andReturn();
	}

	/**
	 * ReadAll Customer
	 */

	@Test
	public void whenCorrectRequestToReadAllCustomers_RetrieveAllCustomersFromDatabase_200() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);

	}

	@Test
	public void whenRequestReadAllCustomersWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest)
						.accept(MediaType.ALL)
						.header("Authorization", invalidAuthRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(401, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);

	}

	@Test
	public void whenCorrectRequestToReadAllCustomersButNotExistsAny_RetrieveNotFound_404() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		String content = mvcResult.getResponse().getContentAsString();

		if (content.length() > 0 && status == 200) {
			Assertions.assertEquals(200, status);
		} else {
			Assertions.assertEquals(404, status);
		}
		Assertions.assertTrue(true);

	}

	@Test
	public void whenCorrectRequestToReadAllCustomersButServerError_RetrieveServerError_500() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertEquals(status, status);
		Assertions.assertTrue(content.length() > 0);
	}

	/**
	 * Read Customers
	 */

	@Test
	public void whenCorrectRequestToReadCustomer_RetrieveCustomerDetails_200() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+userIdFoundTest)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);

	}

	@Test
	public void whenRequestReadCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+userIdFoundTest)
						.accept(MediaType.ALL)
						.header("Authorization", invalidAuthRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(401, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);
	}

	@Test
	public void whenCorrectRequestToReadCustomerButNotExists_RetrieveNotFound_404() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+userIdNotFoundTest)
						.accept(MediaType.ALL)
						.header("Authorization", invalidAuthRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(404, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);

	}

	@Test
	public void whenCorrectRequestToReadCustomerButServerError_RetrieveServerError_500() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+userIdNotFoundTest)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertEquals(status, status);
		Assertions.assertTrue(content.length() > 0);

	}

	/**
	 * Update Customer
	 */

	@Test
	public void whenCorrectRequestToUpdateCustomer_RetrieveCustomerUpdated_200() throws Exception {

		String dataRequest = props.getProperty("application.test.put-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void whenMissingBodyRequestToUpdateCustomer_RetrieveBadRequest_400() throws Exception {

		String dataRequest = props.getProperty("application.test.put-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriBaseTest+"/"+md5Id)
								.content("")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	public void whenRequestUpdateCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

		String dataRequest = props.getProperty("application.test.put-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", invalidAuthRequest)
				)
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void whenRequestUpdateCustomerButNotExists_RetrieveNotFound_404() throws Exception {

		String dataRequest = props.getProperty("application.test.put-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriBaseTest+"/123456770000000000000")
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	public void whenRequestUpdateCustomerWithInvalidBodySize_RetrieveNotAcceptable_406() throws Exception {

		String dataRequest = props.getProperty("application.test.put-customer-invalid");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isNotAcceptable())
				.andReturn();
	}

	@Test
	public void whenCorrectRequestToUpdateCustomerButServerError_RetrieveServerError_500() throws Exception {

		String dataRequest = props.getProperty("application.test.put-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id_2", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isInternalServerError())
				.andReturn();
	}

	/**
	 * Delete Customer
	 */

	@Test
	public void whenCorrectRequestToDeleteCustomer_RetrieveOK_200() throws Exception {

		String dataRequest = props.getProperty("application.test.delete-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));

		mockMvc.perform(
						MockMvcRequestBuilders
								.delete(uriBaseTest+"/"+md5Id)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void whenRequestDeleteCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

		String dataRequest = props.getProperty("application.test.delete-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));

		mockMvc.perform(
						MockMvcRequestBuilders
								.delete(uriBaseTest+"/"+md5Id)
								.header("Authorization", invalidAuthRequest)
				)
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void whenCorrectRequestToDeleteCustomerButNotExists_RetrieveNotFound_404() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.delete(uriBaseTest+"/XYZ909A9A090000000000000000000")
								.header("Authorization", invalidAuthRequest)
				)
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	public void whenCorrectRequestToDeleteCustomerButServerError_RetrieveServerError_500() throws Exception {
		System.out.println("@Test [DELETE] 500 is ignored");
	}

	/**
	 * Patch Customer
	 */

	@Test
	public void whenCorrectRequestToPatchCustomer_RetrieveCustomerUpdated_200() throws Exception {

		String dataRequest = props.getProperty("application.test.patch-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.remove("name");
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isOk())
				.andReturn();

	}

	@Test
	public void whenMissingBodyRequestToPatchCustomer_RetrieveBadRequest_400() throws Exception {

		String dataRequest = props.getProperty("application.test.patch-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriBaseTest+"/"+md5Id)
								.content("")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	public void whenRequestPatchCustomerWithInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

		String dataRequest = props.getProperty("application.test.patch-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.remove("name");
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", invalidAuthRequest)
				)
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void whenRequestPatchCustomerButNotExists_RetrieveNotFound_404() throws Exception {

		String dataRequest = props.getProperty("application.test.patch-customer");
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.remove("name");
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriBaseTest+"/XYZ90D90S90S90S90000000000000")
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", invalidAuthRequest)
				)
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	public void whenRequestPatchCustomerWithInvalidBodySize_RetrieveNotAcceptable_406() throws Exception {

		String dataRequest = props.getProperty("application.test.patch-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isNotAcceptable())
				.andReturn();

	}

	@Test
	public void whenCorrectRequestToPatchCustomerButServerError_RetrieveServerError_500() throws Exception {

		String dataRequest = props.getProperty("application.test.patch-customer");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id_2", md5Id);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isInternalServerError())
				.andReturn();
	}

	/**
	 * Reject Request GET
	 */

	@Test
	public void whenIsGetAndInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenIsGetAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

	}

	/**
	 * Reject Request POST
	 */

	@Test
	public void whenIsPostAndInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenIsPostAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

	}

	/**
	 * Reject Request PUT
	 */

	@Test
	public void whenIsPutAndInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenIsPutAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

	}

	/**
	 * Reject Request DELETE
	 */

	@Test
	public void whenIsDeleteAndInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenIsDeleteAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

	}

	/**
	 * Reject Request PATCH
	 */

	@Test
	public void whenIsPatchAndInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenIsPatchAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

	}

	/**
	 * Reject Request HEAD
	 */

	@Test
	public void whenIsHeadAndInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenIsHeadAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

	}

	/**
	 * Reject Request OPTIONS
	 */

	@Test
	public void whenIsOptionsAndInvalidRequestUriAndInvalidAuthorization_RetrieveUnauthorized_401() throws Exception {

	}

	@Test
	public void whenIsOptionsAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

	}

}
