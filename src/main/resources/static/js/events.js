async function fetchEvents() {
    renderSkeleton('events-results');
    const params = {
        city:                document.getElementById('ev-city').value.trim(),
        eventType:           document.getElementById('ev-eventType').value,
        workMode:            document.getElementById('ev-workMode').value,
        theme:               document.getElementById('ev-theme').value.trim(),
        startDateFrom:       document.getElementById('ev-startDateFrom').value,
        startDateTo:         document.getElementById('ev-startDateTo').value,
        professionalAllowed: document.getElementById('ev-professionalAllowed').value,
        showRejected:        document.getElementById('ev-showRejected').checked ? 'true' : ''
    };
    try {
        const data = await apiFetch('/api/events' + buildQueryString(params));
        if (!data.length) { renderEmpty('events-results', 'No events found. Try different filters or import data.'); return; }
        document.getElementById('events-results').innerHTML = data.map(renderEventCard).join('');
    } catch (e) {
        renderEmpty('events-results', 'Failed to fetch: ' + e.message);
        showToast(e.message, true);
    }
}

async function eventsUpdateStatus(id, status) {
    try {
        await apiFetch(`/api/events/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) });
        showToast(`Status → ${status}`);
        fetchEvents();
    } catch (e) {
        showToast(e.message, true);
    }
}

function renderEventCard(ev) {
    const typeStyle = ev.eventType === 'HACKATHON'
        ? 'bg-purple-500/20 text-purple-400'
        : 'bg-blue-500/20 text-blue-400';

    const applyBtn  = actionBtn('✓ Applied', `eventsUpdateStatus(${ev.id},'APPLIED')`,  ev.status === 'APPLIED');
    const rejectBtn = actionBtn('✗ Reject',  `eventsUpdateStatus(${ev.id},'REJECTED')`, ev.status === 'REJECTED');
    const resetBtn  = ev.status !== 'SAVED'
        ? actionBtn('↩ Reset', `eventsUpdateStatus(${ev.id},'SAVED')`, false)
        : '';

    return `
    <div class="bg-gray-800 border border-gray-700 rounded-xl p-5 flex flex-col gap-3 hover:border-gray-600 transition-colors">
        <div class="flex items-start justify-between gap-2">
            <h3 class="font-semibold text-white text-sm leading-snug">${escHtml(ev.title)}</h3>
            ${statusBadge(ev.status)}
        </div>
        <div class="flex flex-wrap gap-1.5">
            <span class="px-2 py-0.5 rounded text-xs font-medium ${typeStyle}">${ev.eventType}</span>
            ${ev.workMode ? `<span class="px-2 py-0.5 rounded text-xs font-medium bg-gray-700 text-gray-300">${ev.workMode}</span>` : ''}
            ${ev.professionalAllowed ? `<span class="px-2 py-0.5 rounded text-xs font-medium bg-indigo-500/20 text-indigo-400">Pros OK</span>` : ''}
        </div>
        <div class="text-xs text-gray-400 space-y-1">
            ${ev.organizer ? `<div>👤 ${escHtml(ev.organizer)}</div>` : ''}
            ${ev.city     ? `<div>📍 ${escHtml(ev.city)}</div>` : ''}
            ${ev.theme    ? `<div>🎯 ${escHtml(ev.theme)}</div>` : ''}
            ${ev.startDate ? `<div>📅 ${ev.startDate}${ev.endDate ? ' → ' + ev.endDate : ''}</div>` : ''}
            ${ev.registrationDeadline ? `<div>⏰ Deadline: ${ev.registrationDeadline}</div>` : ''}
        </div>
        ${ev.description ? `<p class="text-xs text-gray-500 line-clamp-2">${escHtml(ev.description)}</p>` : ''}
        <div class="flex flex-wrap items-center gap-2 mt-auto pt-1">
            ${applyBtn}${rejectBtn}${resetBtn}
            <a href="${escHtml(ev.registrationLink)}" target="_blank" rel="noopener"
                class="ml-auto text-xs text-indigo-400 hover:text-indigo-300 underline whitespace-nowrap">Register →</a>
        </div>
    </div>`;
}
