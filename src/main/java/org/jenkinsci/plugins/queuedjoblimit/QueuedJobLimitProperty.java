package org.jenkinsci.plugins.queuedjoblimit;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import jenkins.model.Jenkins;
import jenkins.model.OptionalJobProperty;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class QueuedJobLimitProperty extends OptionalJobProperty<Job<?,?>> {
	private int maxQueueEntries;
	//private boolean limitEnabled;
	
	@DataBoundConstructor
	public QueuedJobLimitProperty(int maxQueueEntries) {
		this.maxQueueEntries = maxQueueEntries;
	}
	
	public int getLimit() {
		return getMaxQueueEntries();
	}
	
	public int getMaxQueueEntries() {
		return maxQueueEntries;
	}
	
	public static DescriptorImpl fetchDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(DescriptorImpl.class);
    }


	@Extension
	//@Symbol("queuedJobLimit")
	public static class DescriptorImpl extends OptionalJobPropertyDescriptor {
		private int maxQueueEntries;
		
		@Override
        public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return super.newInstance(req, formData);
            
        }
		
		@Override
        public String getDisplayName() {
            return "Limit number of queued entries";
        }
		
		public int getMaxQueueEntries() {
			return maxQueueEntries;
		}
		
		@Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindJSON(this, formData);
            save();
            return true;
        }
		
	}
}