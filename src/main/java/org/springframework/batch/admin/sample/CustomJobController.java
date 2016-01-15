package org.springframework.batch.admin.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.batch.admin.service.JobService;
import org.springframework.batch.admin.web.JobInfo;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/api")
public class CustomJobController {
	private JobService jobService;


	@RequestMapping(value = "/jobs", method = RequestMethod.GET)
	public List<JobInfo> jobs(@RequestParam(defaultValue = "0") int startJob,
			@RequestParam(defaultValue = "20") int pageSize) {
			
		Collection<String> names = jobService.listJobs(startJob, pageSize);
		List<JobInfo> jobs = new ArrayList<JobInfo>();
		for (String name : names) {
			int count = 0;
			try {
				count = jobService.countJobExecutionsForJob(name);
			}
			catch (NoSuchJobException e) {
				// shouldn't happen
			}
			boolean launchable = jobService.isLaunchable(name);
			jobs.add(new JobInfo(name, count, launchable));
		}
		return jobs;
	}

}
