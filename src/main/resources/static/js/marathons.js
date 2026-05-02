async function fetchMarathons() {
    renderSkeleton('marathons-results');
    const params = {
        distanceCategory: document.getElementById('mr-distanceCategory').value.trim(),
        isFree:           document.getElementById('mr-isFree').value,
        maxFee:           document.getElementById('mr-maxFee').value.trim(),
        dateFrom:         document.getElementById('mr-dateFrom').value,
        dateTo:           document.getElementById('mr-dateTo').value,
        showRejected:     document.getElementById('mr-showRejected').checked ? 'true' : ''
    };
    try {
        const data = await apiFetch('/api/marathons' + buildQueryString(params));
        if (!data.length) { renderEmpty('marathons-results', 'No marathons found. Try different filters or import data.'); return; }
        document.getElementById('marathons-results').innerHTML = data.map(renderMarathonCard).join('');
    } catch (e) {
        renderEmpty('marathons-results', 'Failed to fetch: ' + e.message);
        showToast(e.message, true);
    }
}

async function marathonsUpdateStatus(id, status) {
    try {
        await apiFetch(`/api/marathons/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) });
        showToast(`Status → ${status}`);
        fetchMarathons();
    } catch (e) {
        showToast(e.message, true);
    }
}

function renderMarathonCard(mr) {
    const applyBtn  = actionBtn('✓ Registered', `marathonsUpdateStatus(${mr.id},'APPLIED')`,  mr.status === 'APPLIED');
    const rejectBtn = actionBtn('✗ Skip',        `marathonsUpdateStatus(${mr.id},'REJECTED')`, mr.status === 'REJECTED');
    const resetBtn  = mr.status !== 'SAVED'
        ? actionBtn('↩ Reset', `marathonsUpdateStatus(${mr.id},'SAVED')`, false)
        : '';

    const feeDisplay = mr.isFree
        ? `<span class="px-2 py-0.5 rounded text-xs font-medium bg-green-500/20 text-green-400">Free</span>`
        : mr.entryFee != null
            ? `<span class="px-2 py-0.5 rounded text-xs font-medium bg-gray-700 text-gray-300">₹${mr.entryFee}</span>`
            : '';

    const categories = (mr.distanceCategories || [])
        .map(d => `<span class="px-2 py-0.5 rounded text-xs font-medium bg-orange-500/20 text-orange-400">${escHtml(d)}</span>`)
        .join('');

    return `
    <div class="bg-gray-800 border border-gray-700 rounded-xl p-5 flex flex-col gap-3 hover:border-gray-600 transition-colors">
        <div class="flex items-start justify-between gap-2">
            <h3 class="font-semibold text-white text-sm leading-snug">${escHtml(mr.title)}</h3>
            ${statusBadge(mr.status)}
        </div>
        <div class="flex flex-wrap gap-1.5">
            ${feeDisplay}
            ${categories}
        </div>
        <div class="text-xs text-gray-400 space-y-1">
            ${mr.organizer           ? `<div>👤 ${escHtml(mr.organizer)}</div>` : ''}
            ${mr.location            ? `<div>📍 ${escHtml(mr.location)}, Bangalore</div>` : ''}
            ${mr.date                ? `<div>📅 Race Day: ${mr.date}</div>` : ''}
            ${mr.registrationDeadline ? `<div>⏰ Deadline: ${mr.registrationDeadline}</div>` : ''}
        </div>
        ${mr.description ? `<p class="text-xs text-gray-500 line-clamp-2">${escHtml(mr.description)}</p>` : ''}
        <div class="flex flex-wrap items-center gap-2 mt-auto pt-1">
            ${applyBtn}${rejectBtn}${resetBtn}
            <a href="${escHtml(mr.registrationLink)}" target="_blank" rel="noopener"
                class="ml-auto text-xs text-indigo-400 hover:text-indigo-300 underline whitespace-nowrap">Register →</a>
        </div>
    </div>`;
}
