package org.jenkinsci.plugins.queuedjoblimit;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class QueuedJobLimitJobProperty<J extends Job<?,?>> extends JobProperty<J> {
	private int maxNumOfQueueEntries;

	@DataBoundConstructor
	public QueuedJobLimitJobProperty() {
	}
	
	public QueuedJobLimitJobProperty(int maxQueueEntries) {
		this.maxNumOfQueueEntries = maxQueueEntries;
	}
	
	public int getLimit() {
		return maxNumOfQueueEntries;
	}


	@Extension
	@Symbol("queuedJobLimit")
	public static class DescriptorImpl extends JobPropertyDescriptor {
		 
		@Override
        public String getDisplayName() {
            return "Queued Job Limit Property";
        }
		
		
	}
}