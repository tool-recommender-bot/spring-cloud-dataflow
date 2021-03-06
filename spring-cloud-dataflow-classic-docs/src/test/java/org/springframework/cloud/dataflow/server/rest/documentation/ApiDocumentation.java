/*
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.server.rest.documentation;

import javax.servlet.RequestDispatcher;

import org.junit.Test;

import org.springframework.cloud.dataflow.rest.Version;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Gunnar Hillert
 */
public class ApiDocumentation extends BaseDocumentation {

	@Test
	public void headers() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk())
				.andDo(this.documentationHandler.document(responseHeaders(headerWithName("Content-Type")
						.description("The Content-Type of the payload, e.g. " + "`application/hal+json`"))));
	}

	@Test
	public void errors() throws Exception {
		this.mockMvc
				.perform(get("/error").requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
						.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/apps").requestAttr(
								RequestDispatcher.ERROR_MESSAGE,
								"The app 'http://localhost:8080/apps/123' does " + "not exist"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("error", is("Bad Request")))
				.andExpect(jsonPath("timestamp", is(notNullValue()))).andExpect(jsonPath("status", is(400)))
				.andExpect(jsonPath("path", is(notNullValue())))
				.andDo(this.documentationHandler.document(responseFields(
						fieldWithPath("error").description(
								"The HTTP error that occurred, e.g. `Bad Request`"),
						fieldWithPath("message").description("A description of the cause of the error"),
						fieldWithPath("path").description("The path to which the request was made"),
						fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
						fieldWithPath("timestamp")
								.description("The time, in milliseconds, at which the error" + " occurred"))));
	}

	@Test
	public void index() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andDo(this.documentationHandler.document(links(
				linkWithRel("about").description(
						"Access meta information, including enabled " + "features, security info, version information"),

				linkWithRel("dashboard").description("Access the dashboard UI"),
				linkWithRel("audit-records").description("Provides audit trail information"),
				linkWithRel("apps").description("Handle registered applications"),
				linkWithRel("completions/stream").description("Exposes the DSL completion features " + "for Stream"),
				linkWithRel("completions/task").description("Exposes the DSL completion features for " + "Task"),
				linkWithRel("runtime/streams").description("Exposes stream runtime status"),
				linkWithRel("jobs/executions").description("Provides the JobExecution resource"),
				linkWithRel("jobs/thinexecutions").description("Provides the JobExecution thin resource with no step executions included"),
				linkWithRel("jobs/executions/execution")
						.description("Provides details for a specific" + " JobExecution"),
				linkWithRel("jobs/executions/execution/steps")
						.description("Provides the steps for a " + "JobExecution"),
				linkWithRel("jobs/executions/execution/steps/step")
						.description("Returns the details " + "for a specific step"),
				linkWithRel("jobs/executions/execution/steps/step/progress")
						.description("Provides " + "progress information for a specific step"),
				linkWithRel("jobs/executions/name").description("Retrieve Job Executions by Job name"),
				linkWithRel("jobs/thinexecutions/name").description("Retrieve Job Executions by Job name with no step executions included"),
				linkWithRel("jobs/instances/instance")
						.description("Provides the job instance " + "resource for a specific job instance"),
				linkWithRel("jobs/instances/name")
						.description("Provides the Job instance resource " + "for a specific job name"),
				linkWithRel("runtime/apps").description("Provides the runtime application resource"),
				linkWithRel("runtime/apps/app").description("Exposes the runtime status for a " + "specific app"),
				linkWithRel("runtime/apps/instances").description("Provides the status for app " + "instances"),
				linkWithRel("tasks/definitions").description("Provides the task definition resource"),
				linkWithRel("tasks/definitions/definition")
						.description("Provides details for a " + "specific task definition"),
				linkWithRel("tasks/validation").description("Provides the validation for a task definition"),
				linkWithRel("tasks/executions")
						.description("Returns Task executions and allows launching of tasks"),
				linkWithRel("tasks/executions/current")
						.description("Provides the current count of running tasks"),
				linkWithRel("tasks/schedules")
						.description("Provides schedule information of tasks"),
				linkWithRel("tasks/schedules/instances")
						.description("Provides schedule information of a specific task	"),
				linkWithRel("tasks/executions/name")
						.description("Returns all task executions for a " + "given Task name"),
				linkWithRel("tasks/executions/execution")
						.description("Provides details for a " + "specific task execution"),
				linkWithRel("tasks/platforms")
						.description("Provides platform accounts for launching tasks"),
				linkWithRel("streams/definitions").description("Exposes the Streams resource"),
				linkWithRel("streams/definitions/definition").description("Handle a specific Stream " + "definition"),
				linkWithRel("streams/validation").description("Provides the validation for a stream definition"),
				linkWithRel("streams/deployments").description("Provides Stream deployment operations"),
				linkWithRel("streams/deployments/{name}").description("Request un-deployment of an existing stream"),
				linkWithRel("streams/deployments/deployment")
						.description("Request (un-)deployment of" + " an existing stream definition"),
				linkWithRel("streams/deployments/manifest/{name}/{version}")
						.description("Return a manifest info of a release version"),
				linkWithRel("streams/deployments/history/{name}")
						.description("Get stream's deployment history as list or Releases for this release"),
				linkWithRel("streams/deployments/rollback/{name}/{version}")
						.description("Rollback the stream to the previous or a specific version of the stream"),
				linkWithRel("streams/deployments/update/{name}").description("Update the stream."),
				linkWithRel("streams/deployments/platform/list").description("List of supported deployment platforms"),
				linkWithRel("counters").description("Exposes the resource for dealing with Counters"),
				linkWithRel("counters/counter").description("Handle a specific counter"),
				linkWithRel("aggregate-counters")
						.description("Provides the resource for dealing with" + " aggregate counters"),
				linkWithRel("aggregate-counters/counter").description("Handle a specific aggregate " + "counter"),
				linkWithRel("field-value-counters")
						.description("Provides the resource for dealing " + "with field-value-counters"),
				linkWithRel("field-value-counters/counter").description("Handle a specific " + "field-value-counter"),
				linkWithRel("tools/parseTaskTextToGraph")
						.description("Parse a task definition into a" + " graph structure"),
				linkWithRel("tools/convertTaskGraphToText")
						.description("Convert a graph format into " + "DSL text format")),
				responseFields(fieldWithPath("_links").description("Links to other resources"),
						fieldWithPath("['" + Version.REVISION_KEY + "']")
								.description("Incremented each time " + "a change is implemented in this REST API"),
						fieldWithPath("_links.audit-records.href").description("Link to the audit records"),
						fieldWithPath("_links.dashboard.href").description("Link to the dashboard"),
						fieldWithPath("_links.streams/definitions.href").description("Link to the streams/definitions"),
						fieldWithPath("_links.streams/definitions/definition.href").description("Link to the streams/definitions/definition"),
						fieldWithPath("_links.streams/definitions/definition.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link streams/definitions/definition is templated"),
						fieldWithPath("_links.runtime/apps.href").description("Link to the runtime/apps"),
						fieldWithPath("_links.runtime/apps/app.href").description("Link to the runtime/apps/app"),
						fieldWithPath("_links.runtime/apps/app.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link runtime/apps/app is templated"),
						fieldWithPath("_links.runtime/apps/instances.href").description("Link to the runtime/apps/instances"),
						fieldWithPath("_links.runtime/apps/instances.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link runtime/apps/instances is templated"),
						fieldWithPath("_links.runtime/streams.href").description("Link to the runtime/streams"),
						fieldWithPath("_links.streams/deployments.href").description("Link to the streams/deployments"),
						fieldWithPath("_links.streams/deployments/{name}.href").description("Link to the streams/deployments/{name}"),
						fieldWithPath("_links.streams/deployments/{name}.templated").type(JsonFieldType.BOOLEAN).optional()
								.description("Link streams/deployments/{name} is templated"),
						fieldWithPath("_links.streams/deployments/deployment.href").description("Link to the streams/deployments/deployment"),
						fieldWithPath("_links.streams/deployments/deployment.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link streams/deployments/deployment is templated"),
						fieldWithPath("_links.streams/deployments/manifest/{name}/{version}.href").description("Link to the streams/deployments/manifest/{name}/{version}"),
						fieldWithPath("_links.streams/deployments/manifest/{name}/{version}.templated").type(JsonFieldType.BOOLEAN).optional()
								.description("Link streams/deployments/manifest/{name}/{version} is templated"),
						fieldWithPath("_links.streams/deployments/history/{name}.href").description("Link to the streams/deployments/history/{name}"),
						fieldWithPath("_links.streams/deployments/history/{name}.templated").type(JsonFieldType.BOOLEAN).optional()
								.description("Link streams/deployments/history is templated"),
						fieldWithPath("_links.streams/deployments/rollback/{name}/{version}.href").description("Link to the streams/deployments/rollback/{name}/{version}"),
						fieldWithPath("_links.streams/deployments/rollback/{name}/{version}.templated").type(JsonFieldType.BOOLEAN).optional()
								.description("Link streams/deployments/rollback/{name}/{version} is templated"),
						fieldWithPath("_links.streams/deployments/update/{name}.href").description("Link to the streams/deployments/update/{name}"),
						fieldWithPath("_links.streams/deployments/update/{name}.templated").type(JsonFieldType.BOOLEAN).optional()
								.description("Link streams/deployments/update/{name} is templated"),
						fieldWithPath("_links.streams/deployments/platform/list.href").description("Link to the streams/deployments/platform/list"),
						fieldWithPath("_links.streams/validation.href").description("Link to the streams/validation"),
						fieldWithPath("_links.streams/validation.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link streams/validation is templated"),
						fieldWithPath("_links.tasks/platforms.href").description("Link to the tasks/platforms"),
						fieldWithPath("_links.tasks/definitions.href").description("Link to the tasks/definitions"),
						fieldWithPath("_links.tasks/definitions/definition.href").description("Link to the tasks/definitions/definition"),
						fieldWithPath("_links.tasks/definitions/definition.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link tasks/definitions/definition is templated"),
						fieldWithPath("_links.tasks/executions.href").description("Link to the tasks/executions"),
						fieldWithPath("_links.tasks/executions/name.href").description("Link to the tasks/executions/name"),
						fieldWithPath("_links.tasks/executions/name.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link tasks/executions/name is templated"),
						fieldWithPath("_links.tasks/executions/current.href").description("Link to the tasks/executions/current"),
						fieldWithPath("_links.tasks/executions/execution.href").description("Link to the tasks/executions/execution"),
						fieldWithPath("_links.tasks/executions/execution.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link tasks/executions/execution is templated"),
						fieldWithPath("_links.tasks/schedules.href").description("Link to the tasks/executions/schedules"),
						fieldWithPath("_links.tasks/schedules/instances.href").description("Link to the tasks/schedules/instances"),
						fieldWithPath("_links.tasks/schedules/instances.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link tasks/schedules/instances is templated"),
						fieldWithPath("_links.tasks/validation.href").description("Link to the tasks/validation"),
						fieldWithPath("_links.tasks/validation.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link tasks/validation is templated"),
						fieldWithPath("_links.jobs/executions.href").description("Link to the jobs/executions"),
						fieldWithPath("_links.jobs/thinexecutions.href").description("Link to the jobs/thinexecutions"),
						fieldWithPath("_links.jobs/executions/name.href").description("Link to the jobs/executions/name"),
						fieldWithPath("_links.jobs/executions/name.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link jobs/executions/name is templated"),
						fieldWithPath("_links.jobs/thinexecutions/name.href").description("Link to the jobs/thinexecutions/name"),
						fieldWithPath("_links.jobs/thinexecutions/name.templated").type(JsonFieldType.BOOLEAN).optional()
								.description("Link jobs/executions/name is templated"),
						fieldWithPath("_links.jobs/executions/execution.href").description("Link to the jobs/executions/execution"),
						fieldWithPath("_links.jobs/executions/execution.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link jobs/executions/execution is templated"),
						fieldWithPath("_links.jobs/executions/execution/steps.href").description("Link to the jobs/executions/execution/steps"),
						fieldWithPath("_links.jobs/executions/execution/steps.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link jobs/executions/execution/steps is templated"),
						fieldWithPath("_links.jobs/executions/execution/steps/step.href").description("Link to the jobs/executions/execution/steps/step"),
						fieldWithPath("_links.jobs/executions/execution/steps/step.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link jobs/executions/execution/steps/step is templated"),
						fieldWithPath("_links.jobs/executions/execution/steps/step/progress.href").description("Link to the jobs/executions/execution/steps/step/progress"),
						fieldWithPath("_links.jobs/executions/execution/steps/step/progress.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link jobs/executions/execution/steps/step/progress is templated"),
						fieldWithPath("_links.jobs/instances/name.href").description("Link to the jobs/instances/name"),
						fieldWithPath("_links.jobs/instances/name.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link jobs/instances/name is templated"),
						fieldWithPath("_links.jobs/instances/instance.href").description("Link to the jobs/instances/instance"),
						fieldWithPath("_links.jobs/instances/instance.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link jobs/instances/instance is templated"),
						fieldWithPath("_links.tools/parseTaskTextToGraph.href").description("Link to the tools/parseTaskTextToGraph"),
						fieldWithPath("_links.tools/convertTaskGraphToText.href").description("Link to the tools/convertTaskGraphToText"),
						fieldWithPath("_links.counters.href").description("Link to the counters"),
						fieldWithPath("_links.counters/counter.href").description("Link to the counters/counter"),
						fieldWithPath("_links.counters/counter.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link counters/counter is templated"),
						fieldWithPath("_links.field-value-counters.href").description("Link to the field-value-counters"),
						fieldWithPath("_links.field-value-counters/counter.href").description("Link to the field-value-counters/counter"),
						fieldWithPath("_links.field-value-counters/counter.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link field-value-counters/counter is templated"),
						fieldWithPath("_links.aggregate-counters.href").description("Link to the aggregate-counters"),
						fieldWithPath("_links.aggregate-counters/counter.href").description("Link to the aggregate-counters/counter"),
						fieldWithPath("_links.aggregate-counters/counter.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link aggregate-counters/counter is templated"),
						fieldWithPath("_links.apps.href").description("Link to the apps"),
						fieldWithPath("_links.about.href").description("Link to the about"),
						fieldWithPath("_links.completions/stream.href").description("Link to the completions/stream"),
						fieldWithPath("_links.completions/stream.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link completions/stream is templated"),
						fieldWithPath("_links.completions/task.href").description("Link to the completions/task"),
						fieldWithPath("_links.completions/task.templated").type(JsonFieldType.BOOLEAN).optional()
							.description("Link completions/task is templated")
		)));
	}
}
