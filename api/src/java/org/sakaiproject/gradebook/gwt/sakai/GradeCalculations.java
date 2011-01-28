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
package org.sakaiproject.gradebook.gwt.sakai;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.sakaiproject.gradebook.gwt.client.model.type.CategoryType;
import org.sakaiproject.gradebook.gwt.sakai.model.GradeItem;
import org.sakaiproject.gradebook.gwt.sakai.model.GradeStatistics;
import org.sakaiproject.gradebook.gwt.sakai.model.StudentScore;
import org.sakaiproject.tool.gradebook.Assignment;
import org.sakaiproject.tool.gradebook.AssignmentGradeRecord;
import org.sakaiproject.tool.gradebook.Category;
import org.sakaiproject.tool.gradebook.Gradebook;
import org.sakaiproject.tool.gradebook.LetterGradePercentMapping;

public interface GradeCalculations {

	public static final RoundingMode DISPLAY_ROUNDING = RoundingMode.HALF_UP;

	/**
	 * This method calculates the ratio of 1 divided by numberOfItems
	 * 
	 * @param numberOfItems
	 * @return 1 if numberOfItems <= 1, otherwise 1 / numberOfItems
	 */
	public Double calculateEqualWeight(int numberOfItems);

	/**
	 * If the requestedItemWeight is null, then the weight is equals the requestedItemPoints
	 * If the requestedItemWeight is not null, then the weight is equals the requestedItemWeight
	 * Then it calculates the ratio via weight / 100 and returns the result
	 * 
	 * @param requestedItemWeight
	 * @param requestedItemPoints
	 * @return ratio of either requestedItemWeight / 100 or requestedItemPoints / 100
	 */
	public Double calculateItemWeightAsPercentage(Double requestedItemWeight, Double requestedItemPoints);

	/**
	 *  This method calculates:
	 *  For example: 
	 *  
	 *  (a) If percentGrade is 60, sumCategoryPercents is 100, and assignmentWeight is 0.25, then this item should be worth 15 % of the course grade
	 *  
	 *  	15 = ( 60 * .25 ) / 1
	 *  	
	 *  (b) If percentGrade is 60, sumCategoryPercents is 80, and assignmentWeight is 0.25, then this item should be worth > 15 % of the course grade
	 * 
	 * 		x  = ( 60 * .25 ) / .8
	 * 
	 * @param percentGrade
	 * @param sumCategoryPercents
	 * @param assignmentWeight
	 * @param doNormalizeTo100
	 * @return zero if either one of percentGrade, sumCategoryPercents, assignmentWeight is zero, otherwise the result from the above calculation
	 */
	public BigDecimal calculateItemGradePercent(BigDecimal percentGrade, BigDecimal sumCategoryPercents, BigDecimal assignmentWeight, boolean doNormalizeTo100);

	/**
	 * This method compares the percentage argument against the various letter grade boundary values
	 * and returns the corresponding letter grade.
	 * 
	 * @param percentage
	 * @return letter grade that corresponds to the provided percentage grade
	 */
	public String convertPercentageToLetterGrade(BigDecimal percentage);

	/**
	 * This method looks up the letter grade in the letterGradeMap and returns the corresponding 
	 * grade percentage value
	 * 
	 * @param letterGrade
	 * @return percentage grade that corresponds to the provided letter grade
	 */
	public Double convertLetterGradeToPercentage(String letterGrade);

	/**
	 * This method checks the letterGradeMap if the provided letterGrade exists
	 * 
	 * Created GRBK-816 to verify the implementation of this method.
	 * 
	 * @param letterGrade
	 * @return true if letterGrade is null or letterGrade length is 0, otherwise true or false 
	 * depending if the letter grade is present in the letterGradeMap
	 * 
	 */
	public boolean isValidLetterGrade(String letterGrade);

	/**
	 * This method converts the Double method arguments to BigDecimal values and then calculates:
	 * 
	 * ratio = maxPointValue / maxPointStartValue
	 * points = pointValue * ratio
	 * 
	 * @param pointValue
	 * @param maxPointValue
	 * @param maxPointStartValue
	 * @return points
	 */
	public BigDecimal getNewPointsGrade(Double pointValue, Double maxPointValue, Double maxPointStartValue);

	/**
	 * The method converts the Double method arguments to BigDeciaml values and the calculates:
	 * 
	 * pointsEarned = (percentage / 100) * assignment.getPointsPossible()
	 * 
	 * @param assignment
	 * @param percentage
	 * @return pointsEarned
	 */
	public BigDecimal getPercentAsPointsEarned(Assignment assignment, Double percentage);

	/**
	 * This method sums up the category points and percentages taking into account factors such as
	 * excused and drop lowest grade items. 
	 * 
	 * @param category
	 * @param assignments
	 * @param isWeighted
	 * @param isCategoryExtraCredit
	 * @return a BigDecimal array containing totalCategoryPercent at [0] and totalCategoryPoints at [1]
	 */
	public BigDecimal[] calculatePointsCategoryPercentSum(Category category, List<Assignment> assignments, boolean isWeighted, boolean isCategoryExtraCredit); 

	/**
	 * This method sums up the category points and percentages taking into account factors such as
	 * excused, weighed, and drop lowest grade items.
	 * 
	 * @param category
	 * @param assignments
	 * @param categoryType
	 * @param isCategoryExtraCredit
	 * @return a BigDecimal array containing totalCategoryPercent at [0] and totalCategoryPoints at [1]
	 */
	public BigDecimal[] calculatePointsCategoryPercentSum(GradeItem category, List<GradeItem> assignments, CategoryType categoryType, boolean isCategoryExtraCredit); 

	/**
	 * 
	 * @param assignment
	 * @param percentGrade
	 * @param percentCategorySum
	 * @param pointSum
	 * @param isEnforcePointWeighting
	 * @return
	 */
	public BigDecimal[] calculateCourseGradeCategoryPercents(Assignment assignment, BigDecimal percentGrade, BigDecimal percentCategorySum, BigDecimal pointSum, boolean isEnforcePointWeighting);

	/**
	 * 
	 * @param assignment
	 * @param percentGrade
	 * @param percentCategorySum
	 * @param pointSum
	 * @param isEnforcePointWeighting
	 * @return
	 */
	public BigDecimal[] calculateCourseGradeCategoryPercents(GradeItem assignment, BigDecimal percentGrade, BigDecimal percentCategorySum, BigDecimal pointSum, boolean isEnforcePointWeighting);


	/**
	 * Result = PointsEarned * 100 / PointsPossible
	 * 
	 * @param assignment an assignment
	 * @param assignmentGradeRecord an assignmentGradeRecord that is linked to the assignment, for a specific user 
	 * 
	 * @return points earned as percent for an assignment, or null if assignment or assignmentGradeRecord are null
	 */
	public BigDecimal getPointsEarnedAsPercent(Assignment assignment, AssignmentGradeRecord assignmentGradeRecord);


	/**
	 * Result = The Category Weight while taking into consideration the category "constraints" such as [isRemove, ...]
	 * 
	 * @param category a category
	 * 
	 * @return the category weight, or null if the category is null
	 */
	public BigDecimal getCategoryWeight(Category category);
	
	/**
	 * 
	 * @param gradebook
	 * @param items
	 * @param assignmentGradeRecordMap
	 * @param isExtraCreditScaled
	 * @return
	 */
	public BigDecimal getCourseGrade(Gradebook gradebook, Collection<?> items, Map<Long, AssignmentGradeRecord> assignmentGradeRecordMap, boolean isExtraCreditScaled);

	/**
	 * 
	 * @param gradeList
	 * @param sum
	 * @param rankStudentId
	 * @return
	 */
	public GradeStatistics calculateStatistics(List<StudentScore> gradeList, BigDecimal sum, String rankStudentId);

	/**
	 * 
	 * @param assignment
	 * @param gradeRecordFromCall
	 * @return
	 */
	public Double calculateDoublePointForRecord(Assignment assignment, AssignmentGradeRecord gradeRecordFromCall);

	/**
	 * 
	 * @param assignment
	 * @param letterGradePercentMapping
	 * @param gradeRecordFromCall
	 * @return
	 */
	public Double calculateDoublePointForLetterGradeRecord(Assignment assignment, LetterGradePercentMapping letterGradePercentMapping, AssignmentGradeRecord gradeRecordFromCall);

	/**
	 * 
	 * @return
	 */
	public Map<String, Double> getLetterGradeMap();
	
	/**
	 * 
	 * @param map
	 */
	public void setLetterGradeMap(Map<String, Double> map);
}
