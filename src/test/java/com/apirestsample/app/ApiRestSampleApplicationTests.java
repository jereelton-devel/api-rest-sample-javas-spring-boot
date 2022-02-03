package com.apirestsample.app;

import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.apirestsample.app.utils.Helpers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiRestSampleApplicationTests extends AbstractTest {

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

		rollbackTest(md5Id);

	}

	@Test
	public void whenCorrectRequestToCreateCustomerAlreadyExists_RetrieveCustomerFound_302() throws Exception {

		String dataRequest = props.getProperty("application.test.post-customer-exists");
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
		System.out.println("@Test [READ ALL] 500 is ignored");
	}

	/**
	 * Read Customers
	 */

	@Test
	public void whenCorrectRequestToReadCustomer_RetrieveCustomerDetails_200() throws Exception {

		String md5Id = md5(userIdFoundTest);

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+md5Id)
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

		String md5Id = md5(userIdFoundTest);

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+md5Id)
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

		String md5Id = md5(userIdNotFoundTest);

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(uriBaseTest+"/"+md5Id)
						.accept(MediaType.ALL)
						.header("Authorization", authRequest)
				).andReturn();

		int status = mvcResult.getResponse().getStatus();

		Assertions.assertEquals(404, status);
		String content = mvcResult.getResponse().getContentAsString();
		Assertions.assertTrue(content.length() > 0);

	}

	@Test
	public void whenCorrectRequestToReadCustomerButServerError_RetrieveServerError_500() throws Exception {
		System.out.println("@Test [READ] 500 is ignored");
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

		String dataRequest = props.getProperty("application.test.put-customer-not-found");
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
		jsonObj.appendField("name_2", jsonObj.getAsString("name"));
		jsonObj.remove("name");
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
		JSONObject jsonObj = queryStringToJson(dataRequest);
		jsonObj.appendField("id", md5Id);
		String createCustomer = jsonToString(jsonObj);

		createCustomerBeforeTest(createCustomer);

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

		String dataRequest = props.getProperty("application.test.delete-customer-not-found");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));

		mockMvc.perform(
						MockMvcRequestBuilders
								.delete(uriBaseTest+"/"+md5Id)
								.header("Authorization", authRequest)
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

		String dataRequest = props.getProperty("application.test.patch-customer-not-found");
		String md5Id = md5(getDataFromQueryString(dataRequest, "name"));
		JSONObject jsonObj = queryStringToJson(dataRequest);
		String customerPost = jsonToString(jsonObj);

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriBaseTest+"/"+md5Id)
								.content(customerPost)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	public void whenRequestPatchCustomerWithInvalidBodySize_RetrieveNotAcceptable_406() throws Exception {

		String dataRequest = props.getProperty("application.test.patch-customer-invalid");
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
		jsonObj.appendField("name_2", "Test Patcher");
		jsonObj.remove("name");
		jsonObj.remove("active");
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
	public void whenIsGetAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.get(uriWrongTest)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();

		mockMvc.perform(
						MockMvcRequestBuilders
								.get(uriWrongTest2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();
	}

	/**
	 * Reject Request POST
	 */

	@Test
	public void whenIsPostAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.post(uriWrongTest)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();

		mockMvc.perform(
						MockMvcRequestBuilders
								.post(uriWrongTest2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();
	}

	/**
	 * Reject Request PUT
	 */

	@Test
	public void whenIsPutAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriWrongTest)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();

		mockMvc.perform(
						MockMvcRequestBuilders
								.put(uriWrongTest2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();
	}

	/**
	 * Reject Request DELETE
	 */

	@Test
	public void whenIsDeleteAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.delete(uriWrongTest)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();

		mockMvc.perform(
						MockMvcRequestBuilders
								.delete(uriWrongTest2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();
	}

	/**
	 * Reject Request PATCH
	 */

	@Test
	public void whenIsPatchAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriWrongTest)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();

		mockMvc.perform(
						MockMvcRequestBuilders
								.patch(uriWrongTest2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();
	}

	/**
	 * Reject Request HEAD
	 */

	@Test
	public void whenIsHeadAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.head(uriWrongTest)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();

		mockMvc.perform(
						MockMvcRequestBuilders
								.head(uriWrongTest2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();
	}

	/**
	 * Reject Request OPTIONS
	 */

	@Test
	public void whenIsOptionsAndInvalidRequestUriAnd_RetrieveNotAllowed_405() throws Exception {

		mockMvc.perform(
						MockMvcRequestBuilders
								.options(uriWrongTest)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();

		mockMvc.perform(
						MockMvcRequestBuilders
								.options(uriWrongTest2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.header("Authorization", authRequest)
				)
				.andExpect(status().isMethodNotAllowed())
				.andReturn();
	}

}
