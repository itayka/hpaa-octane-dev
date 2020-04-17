/*
 * Certain versions of software and/or documents ("Material") accessible here may contain branding from
 * Hewlett-Packard Company (now HP Inc.) and Hewlett Packard Enterprise Company.  As of September 1, 2017,
 * the Material is now offered by Micro Focus, a separately owned and operated company.  Any reference to the HP
 * and Hewlett Packard Enterprise/HPE marks is historical in nature, and the HP and Hewlett Packard Enterprise/HPE
 * marks are the property of their respective owners.
 * __________________________________________________________________
 * MIT License
 *
 * (c) Copyright 2012-2019 Micro Focus or one of its affiliates.
 *
 * The only warranties for products and services of Micro Focus and its affiliates
 * and licensors ("Micro Focus") are set forth in the express warranty statements
 * accompanying such products and services. Nothing herein should be construed as
 * constituting an additional warranty. Micro Focus shall not be liable for technical
 * or editorial errors or omissions contained herein.
 * The information contained herein is subject to change without notice.
 * ___________________________________________________________________
 */

package com.microfocus.application.automation.tools.octane.executor;

import com.hp.octane.integrations.utils.CIPluginSDKUtils;
import com.microfocus.application.automation.tools.octane.configuration.SDKBasedLoggerProvider;
import hudson.model.FreeStyleProject;
import hudson.model.ParameterDefinition;
import hudson.model.ParametersDefinitionProperty;
import jenkins.model.Jenkins;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;


/***
 * Recognizer for UFT related jobs
 */
public class UftJobRecognizer {

    private static Logger logger = SDKBasedLoggerProvider.getLogger(UftJobRecognizer.class);
    /**
     * Check if current job is EXECUTOR job
     *
     * @param job
     * @return
     */
    public static boolean isExecutorJob(FreeStyleProject job) {
        boolean isExecutorJob = (job.getName().contains(UftConstants.EXECUTION_JOB_MIDDLE_NAME) ||
                job.getName().startsWith(UftConstants.EXECUTION_JOB_MIDDLE_NAME_WITH_TEST_RUNNERS));
        return isExecutorJob;
    }

    /**
     * Check if current job is discovery job
     *
     * @param job
     * @return
     */
    public static boolean isDiscoveryJob(FreeStyleProject job) {
        boolean isDiscoveryJob = (job.getName().contains(UftConstants.DISCOVERY_JOB_MIDDLE_NAME) ||
                job.getName().startsWith(UftConstants.DISCOVERY_JOB_MIDDLE_NAME_WITH_TEST_RUNNERS));
        return isDiscoveryJob;
    }

    /**
     * Extract executor id from the job
     *
     * @param job
     * @return
     */
    public static String getExecutorId(FreeStyleProject job) {
        ParametersDefinitionProperty parameters = job.getProperty(ParametersDefinitionProperty.class);
        String parameterName = UftConstants.TEST_RUNNER_ID_PARAMETER_NAME;
        if (!parameters.getParameterDefinitionNames().contains(parameterName)) {
            parameterName = UftConstants.EXECUTOR_ID_PARAMETER_NAME;
        }
        ParameterDefinition pd = parameters.getParameterDefinition(parameterName);
        if (pd != null) {
            String value = (String) pd.getDefaultParameterValue().getValue();
            return value;
        } else {
            return null;
        }
    }

    /**
     * Extract executor logical name from the job
     *
     * @param job
     * @return
     */
    public static String getExecutorLogicalName(FreeStyleProject job) {
        ParametersDefinitionProperty parameters = job.getProperty(ParametersDefinitionProperty.class);
        String parameterName = UftConstants.EXECUTOR_LOGICAL_NAME_PARAMETER_NAME;
        if (!parameters.getParameterDefinitionNames().contains(parameterName)) {
            parameterName = UftConstants.TEST_RUNNER_LOGICAL_NAME_PARAMETER_NAME;
        }
        ParameterDefinition pd = parameters.getParameterDefinition(parameterName);
        if (pd != null) {
            String value = (String) pd.getDefaultParameterValue().getValue();
            return value;
        } else {
            return null;
        }
    }

    public static void deleteExecutionJobByExecutorIfNeverExecuted(String executorToDelete) {
        List<FreeStyleProject> jobs = Jenkins.getInstanceOrNull().getAllItems(FreeStyleProject.class);
        for (FreeStyleProject proj : jobs) {
            if (UftJobRecognizer.isExecutorJob(proj) && isJobMatch(executorToDelete, proj)
                    && proj.getLastBuild() == null && !proj.isBuilding() && !proj.isInQueue()) {
                try {
                    logger.warn(String.format("Job '%s' is going to be deleted since matching executor in Octane was deleted and this job was never executed and has no history.", proj.getName()));
                    proj.delete();
                } catch (IOException | InterruptedException e) {
                    logger.error("Failed to delete job  " + proj.getName() + " : " + e.getMessage());
                }
            }
        }
    }

    private static Boolean isJobMatch(String executorToFind, FreeStyleProject proj) {
        String executorId = UftJobRecognizer.getExecutorId(proj);
        String executorLogicalName = UftJobRecognizer.getExecutorLogicalName(proj);
        return (executorId != null && executorId.equals(executorToFind)) ||
                (executorLogicalName != null && executorLogicalName.equals(executorToFind));
    }

    /**
     * Delete discovery job that related to specific executor in Octane
     *
     * @param executorToDelete
     */
    public static void deleteDiscoveryJobByExecutor(String executorToDelete) {

        List<FreeStyleProject> jobs = Jenkins.getInstanceOrNull().getAllItems(FreeStyleProject.class);
        for (FreeStyleProject proj : jobs) {
            if (UftJobRecognizer.isDiscoveryJob(proj) && isJobMatch(executorToDelete, proj)) {
                if (proj.isBuilding()) {
                    proj.getLastBuild().getExecutor().interrupt();
                    CIPluginSDKUtils.doWait(10000); //wait before deleting the job, so Jenkins will be able to complete some IO actions
                } else if (proj.isInQueue()) {
                    Jenkins.getInstanceOrNull().getQueue().cancel(proj);
                    CIPluginSDKUtils.doWait(10000); //wait before deleting the job, so Jenkins will be able to complete some IO actions
                }

                try {
                    logger.warn(String.format("Job '%s' is going to be deleted since matching executor in Octane was deleted", proj.getName()));
                    proj.delete();
                } catch (Exception e) {
                    logger.error("Failed to delete job  " + proj.getName() + " : " + e.getMessage());
                }
            }
        }
    }
}
