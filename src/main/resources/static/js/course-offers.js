async function fetchCourseOffers() {
    renderSkeleton('course-offers-results');
    const params = {
        platform:    document.getElementById('co-platform').value.trim(),
        topic:       document.getElementById('co-topic').value.trim(),
        maxPrice:    document.getElementById('co-maxPrice').value,
        isFree:      document.getElementById('co-isFree').value,
        showRejected: document.getElementById('co-showRejected').checked ? 'true' : ''
    };
    try {
        const data = await apiFetch('/api/course-offers' + buildQueryString(params));
        if (!data.length) { renderEmpty('course-offers-results', 'No course offers found. Try different filters or import data.'); return; }
        document.getElementById('course-offers-results').innerHTML = data.map(renderCourseOfferCard).join('');
    } catch (e) {
        renderEmpty('course-offers-results', 'Failed to fetch: ' + e.message);
        showToast(e.message, true);
    }
}

async function courseOffersUpdateStatus(id, status) {
    try {
        await apiFetch(`/api/course-offers/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) });
        showToast(`Status → ${status}`);
        fetchCourseOffers();
    } catch (e) {
        showToast(e.message, true);
    }
}

function renderCourseOfferCard(offer) {
    const platformColors = {
        udemy: 'bg-orange-500/20 text-orange-400',
        coursera: 'bg-blue-500/20 text-blue-400',
        udacity: 'bg-teal-500/20 text-teal-400',
        upgrad: 'bg-purple-500/20 text-purple-400',
    };
    const platformKey = (offer.platform || '').toLowerCase();
    const platformStyle = platformColors[platformKey] || 'bg-gray-700 text-gray-300';

    const priceDisplay = offer.isFree
        ? `<span class="text-green-400 font-semibold text-sm">FREE</span>`
        : offer.discountedPrice != null && offer.originalPrice != null && offer.discountedPrice < offer.originalPrice
            ? `<span class="line-through text-gray-500 text-xs mr-1">₹${offer.originalPrice}</span><span class="text-white font-semibold text-sm">₹${offer.discountedPrice}</span>`
            : offer.discountedPrice != null
                ? `<span class="text-white font-semibold text-sm">₹${offer.discountedPrice}</span>`
                : '';

    const saveBtn   = actionBtn('✓ Save',   `courseOffersUpdateStatus(${offer.id},'SAVED')`,    offer.status === 'SAVED');
    const rejectBtn = actionBtn('✗ Reject', `courseOffersUpdateStatus(${offer.id},'REJECTED')`, offer.status === 'REJECTED');

    return `
    <div class="bg-gray-800 border border-gray-700 rounded-xl p-5 flex flex-col gap-3 hover:border-gray-600 transition-colors">
        <div class="flex items-start justify-between gap-2">
            <h3 class="font-semibold text-white text-sm leading-snug">${escHtml(offer.title)}</h3>
            ${statusBadge(offer.status)}
        </div>
        <div class="flex flex-wrap gap-1.5 items-center">
            <span class="px-2 py-0.5 rounded text-xs font-medium ${platformStyle}">${escHtml(offer.platform)}</span>
            ${offer.topic ? `<span class="px-2 py-0.5 rounded text-xs bg-gray-700 text-gray-300">${escHtml(offer.topic)}</span>` : ''}
            ${offer.isFree ? `<span class="px-2 py-0.5 rounded text-xs font-medium bg-green-500/20 text-green-400">Free</span>` : ''}
        </div>
        <div class="text-xs text-gray-400 space-y-1">
            <div>${priceDisplay}</div>
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
