/**********************************************************************************
*
* $Id:$
*
***********************************************************************************
*
* Copyright (c) 2008, 2009 The Regents of the University of California
*
* Licensed under the
* Educational Community License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may
* obtain a copy of the License at
* 
* http://www.osedu.org/licenses/ECL-2.0
* 
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS"
* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
* or implied. See the License for the specific language governing
* permissions and limitations under the License.
*
**********************************************************************************/

package org.sakaiproject.gradebook.gwt.client.model;

public class CategoryModel extends EntityModel {

	private static final long serialVersionUID = 1L;
	
	public enum Key { ID, CATEGORY_DISPLAY_NAME };

	public CategoryModel() {
		super();
	}
	
	public CategoryModel(Long categoryId, String categoryDisplayName) {
		super();
		setCategoryId(categoryId);
		setCategoryDisplayName(categoryDisplayName);
	}
	
	public void setCategoryId(Long categoryId) {
		set(Key.ID.name(), categoryId);
	}
	
	public Long getCategoryId() {
		return get(Key.ID.name());
	}
	
	public void setCategoryDisplayName(String categoryDisplayName) {
		set(Key.CATEGORY_DISPLAY_NAME.name(), categoryDisplayName);
	}
	
	public String getCategoryDisplayName() {
		return get(Key.CATEGORY_DISPLAY_NAME.name());
	}
	
	@Override
	public String getIdentifier() {
		Long categoryId = getCategoryId();
		return (null == categoryId) ? null : getCategoryId().toString();
	}
	
	@Override
	public String getDisplayName() {
		return getIdentifier();
	}
}