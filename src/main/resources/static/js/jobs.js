async function fetchJobs() {
    renderSkeleton('jobs-results');
    const params = {
        location:      document.getElementById('job-location').value.trim(),
        workMode:      document.getElementById('job-workMode').value,
        skills:        document.getElementById('job-skills').value.trim(),
        experienceMin: document.getElementById('job-experienceMin').value,
        experienceMax: document.getElementById('job-experienceMax').value,
        showRejected:  document.getElementById('job-showRejected').checked ? 'true' : ''
    };
    try {
        const data = await apiFetch('/api/jobs' + buildQueryString(params));
        if (!data.length) { renderEmpty('jobs-results', 'No jobs found. Try different filters or import data.'); return; }
        document.getElementById('jobs-results').innerHTML = data.map(renderJobCard).join('');
    } catch (e) {
        renderEmpty('jobs-results', 'Failed to fetch: ' + e.message);
        showToast(e.message, true);
    }
}

async function jobsUpdateStatus(id, status) {
    try {
        await apiFetch(`/api/jobs/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) });
        showToast(`Status → ${status}`);
        fetchJobs();
    } catch (e) {
        showToast(e.message, true);
    }
}

function renderJobCard(job) {
    const modeStyle = { ONLINE: 'bg-teal-500/20 text-teal-400', OFFLINE: 'bg-orange-500/20 text-orange-400', HYBRID: 'bg-indigo-500/20 text-indigo-400' };
    const skillPills = job.skills
        ? job.skills.split(',').map(s => `<span class="px-1.5 py-0.5 rounded bg-gray-700 text-gray-300 text-xs">${escHtml(s.trim())}</span>`).join('')
        : '';

    const applyBtn  = actionBtn('✓ Applied', `jobsUpdateStatus(${job.id},'APPLIED')`,  job.status === 'APPLIED');
    const rejectBtn = actionBtn('✗ Reject',  `jobsUpdateStatus(${job.id},'REJECTED')`, job.status === 'REJECTED');
    const resetBtn  = job.status !== 'SAVED'
        ? actionBtn('↩ Reset', `jobsUpdateStatus(${job.id},'SAVED')`, false)
        : '';

    return `
    <div class="bg-gray-800 border border-gray-700 rounded-xl p-5 flex flex-col gap-3 hover:border-gray-600 transition-colors">
        <div class="flex items-start justify-between gap-2">
            <h3 class="font-semibold text-white text-sm leading-snug">${escHtml(job.title)}</h3>
            ${statusBadge(job.status)}
        </div>
        <div class="flex flex-wrap gap-1.5 items-center">
            <span class="text-sm font-medium text-gray-200">${escHtml(job.company)}</span>
            ${job.workMode ? `<span class="px-2 py-0.5 rounded text-xs font-medium ${modeStyle[job.workMode] || 'bg-gray-700 text-gray-300'}">${job.workMode}</span>` : ''}
        </div>
        <div class="text-xs text-gray-400 space-y-1">
            ${job.location ? `<div>📍 ${escHtml(job.location)}</div>` : ''}
            ${(job.experienceMin != null || job.experienceMax != null) ? `<div>🎓 ${job.experienceMin ?? 0}–${job.experienceMax ?? '?'} yrs experience</div>` : ''}
            ${job.salaryRange ? `<div>💰 ${escHtml(job.salaryRange)}</div>` : ''}
        </div>
        ${skillPills ? `<div class="flex flex-wrap gap-1">${skillPills}</div>` : ''}
        ${job.description ? `<p class="text-xs text-gray-500 line-clamp-2">${escHtml(job.description)}</p>` : ''}
        <div class="flex flex-wrap items-center gap-2 mt-auto pt-1">
            ${applyBtn}${rejectBtn}${resetBtn}
            <a href="${escHtml(job.jobLink)}" target="_blank" rel="noopener"
                class="ml-auto text-xs text-indigo-400 hover:text-indigo-300 underline whitespace-nowrap">View Job →</a>
        </div>
    </div>`;
}
