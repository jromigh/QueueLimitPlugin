package org.jenkinsci.plugins.queuedjoblimit;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.Queue;
import hudson.model.Queue.QueueDecisionHandler;
import hudson.model.Queue.Task;

@Extension
public class QueuedJobLimitDecisionHandler extends Queue.QueueDecisionHandler {
	private static final Logger LOGGER = Logger.getLogger(QueuedJobLimitDecisionHandler.class.getName());
	
	public boolean shouldSchedule(Queue.Task p, List<Action> actions) {
		// This decision handler only cares about JOBS with a QueuedJobLimitProperty
		if (!(p instanceof Job)) {
			return true;
		}
		
		Job<?, ?> queuedJob = (Job<?,?>) p;
		
		QueuedJobLimitJobProperty<?> prop = queuedJob.getProperty(QueuedJobLimitJobProperty.class);
		if (prop == null) {
			return true;
		}
		
		// Now that we've confirmed we have a QueuedJobLimitProperty, get the number.
		int maxNum = prop.getLimit();
		
		// If it is a negative value, that disables limiting in the queue.
		if (maxNum < 0) {
			return true;
		}
		
		// Now that we know the Job is limited, let's check the queue for blocked and pending instances of our job
		String jobName = queuedJob.getFullName();
		Queue queue = Queue.getInstance();
		
		int matches = 0;
		for (Queue.Item item : queue.getItems()) {
			// Ensure this queue item is for a job
			if (item.task instanceof Job) {
				Job<?,?> j = ((Job<?,?>) item.task);
				// If the names match, this should be an entry for our job!
				if (j.getFullName().equals(jobName)) {
					matches++;
				}
			}
		}
		
		// If we haven't yet hit the limit of queued items, allow this request.
		if (matches < maxNum) {
			LOGGER.info("Blocking the queue request from " +
				jobName + " due to already having " + matches + " queued instances. (" + matches + " >= " + maxNum + " max).");
			return true;
		}
		
		// It looks like we've hit our limit!
		return false;
	}
}