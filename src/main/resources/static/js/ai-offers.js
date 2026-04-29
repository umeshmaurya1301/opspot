async function fetchAIOffers() {
    renderSkeleton('ai-offers-results');
    const params = {
        toolName:        document.getElementById('ai-toolName').value.trim(),
        isFree:          document.getElementById('ai-isFree').value,
        forProfessionals: document.getElementById('ai-forProfessionals').value,
        showRejected:    document.getElementById('ai-showRejected').checked ? 'true' : ''
    };
    try {
        const data = await apiFetch('/api/ai-offers' + buildQueryString(params));
        if (!data.length) { renderEmpty('ai-offers-results', 'No AI tool offers found. Try different filters or import data.'); return; }
        document.getElementById('ai-offers-results').innerHTML = data.map(renderAIOfferCard).join('');
    } catch (e) {
        renderEmpty('ai-offers-results', 'Failed to fetch: ' + e.message);
        showToast(e.message, true);
    }
}

async function aiOffersUpdateStatus(id, status) {
    try {
        await apiFetch(`/api/ai-offers/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) });
        showToast(`Status → ${status}`);
        fetchAIOffers();
    } catch (e) {
        showToast(e.message, true);
    }
}

function renderAIOfferCard(offer) {
    const toolColors = {
        cursor:  'bg-purple-500/20 text-purple-400',
        copilot: 'bg-gray-600/40 text-gray-300',
        github:  'bg-gray-600/40 text-gray-300',
        claude:  'bg-orange-500/20 text-orange-400',
        gemini:  'bg-blue-500/20 text-blue-400',
        windsurf:'bg-cyan-500/20 text-cyan-400',
        tabnine: 'bg-green-500/20 text-green-400',
    };
    const toolKey = (offer.toolName || '').toLowerCase().split(' ')[0];
    const toolStyle = toolColors[toolKey] || 'bg-indigo-500/20 text-indigo-400';

    const saveBtn   = actionBtn('✓ Save',   `aiOffersUpdateStatus(${offer.id},'SAVED')`,    offer.status === 'SAVED');
    const rejectBtn = actionBtn('✗ Reject', `aiOffersUpdateStatus(${offer.id},'REJECTED')`, offer.status === 'REJECTED');

    return `
    <div class="bg-gray-800 border border-gray-700 rounded-xl p-5 flex flex-col gap-3 hover:border-gray-600 transition-colors">
        <div class="flex items-start justify-between gap-2">
            <div>
                <span class="px-2 py-0.5 rounded text-xs font-semibold ${toolStyle} mr-2">${escHtml(offer.toolName)}</span>
                <h3 class="font-semibold text-white text-sm leading-snug mt-1.5">${escHtml(offer.offerTitle)}</h3>
            </div>
            ${statusBadge(offer.status)}
        </div>
        <div class="flex flex-wrap gap-1.5">
            ${offer.isFree
                ? `<span class="px-2 py-0.5 rounded text-xs font-medium bg-green-500/20 text-green-400">Free</span>`
                : `<span class="px-2 py-0.5 rounded text-xs font-medium bg-yellow-500/20 text-yellow-400">Paid</span>`}
            ${offer.forProfessionals
                ? `<span class="px-2 py-0.5 rounded text-xs font-medium bg-indigo-500/20 text-indigo-400">For Professionals</span>`
                : ''}
        </div>
        <div class="text-xs text-gray-400 space-y-1">
            ${offer.validTill ? `<div>⏰ Valid till: ${offer.validTill}</div>` : ''}
        </div>
        ${offer.description ? `<p class="text-xs text-gray-500 line-clamp-2">${escHtml(offer.description)}</p>` : ''}
        <div class="flex flex-wrap items-center gap-2 mt-auto pt-1">
            ${saveBtn}${rejectBtn}
            <a href="${escHtml(offer.offerLink)}" target="_blank" rel="noopener"
                class="ml-auto text-xs text-indigo-400 hover:text-indigo-300 underline whitespace-nowrap">View Offer →</a>
        </div>
    </div>`;
}
