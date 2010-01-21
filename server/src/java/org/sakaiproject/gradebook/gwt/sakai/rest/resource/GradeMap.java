package org.sakaiproject.gradebook.gwt.sakai.rest.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.sakaiproject.gradebook.gwt.client.AppConstants;
import org.sakaiproject.gradebook.gwt.client.exceptions.InvalidInputException;

@Path("/gradebook/rest/grademap/{uid}/{id}")
public class GradeMap extends Resource {

	@GET 
    @Produces("application/json")
    public String get(@PathParam("uid") String gradebookUid, @PathParam("id") Long gradebookId) {
		List<Map<String,Object>> list = service.getGradeMaps(gradebookUid);
		return toJson(AppConstants.GRADE_MAP_ROOT, list, list.size());
	}
	
	@PUT @Path("{letterGrade}")
	public void update(@PathParam("uid") String gradebookUid, @PathParam("id") Long gradebookId,
			@PathParam("letterGrade") String letterGrade, String model) throws InvalidInputException {
		
		Map<String,Object> map = fromJson(model, Map.class);
		Object value = map.get("value");
		service.updateGradeMap(gradebookUid, letterGrade, value);
	}
	
	@DELETE
	public void reset(@PathParam("uid") String gradebookUid, @PathParam("id") Long gradebookId) {
		
		service.resetGradeMap(gradebookUid);
	}
	
}