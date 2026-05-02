// Shared utilities

function escHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

async function apiFetch(url, options = {}) {
    const res = await fetch(url, {
        headers: { 'Content-Type': 'application/json' },
        ...options
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || 'Request failed');
    return data;
}

function showToast(message, isError = false) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `fixed bottom-5 right-5 z-50 px-4 py-3 rounded-xl text-sm font-medium shadow-xl max-w-sm transition-all ${
        isError
            ? 'bg-red-900 border border-red-700 text-red-200'
            : 'bg-gray-800 border border-green-700 text-green-300'
    }`;
    toast.classList.remove('hidden');
    clearTimeout(window._toastTimer);
    window._toastTimer = setTimeout(() => toast.classList.add('hidden'), 3500);
}

function buildQueryString(params) {
    const entries = Object.entries(params).filter(([, v]) => v !== '' && v !== null && v !== undefined && v !== false || v === false && params.showRejected !== undefined);
    const filtered = Object.entries(params).filter(([, v]) => v !== '' && v !== null && v !== undefined);
    return filtered.length ? '?' + new URLSearchParams(filtered).toString() : '';
}

function renderSkeleton(containerId, count = 6) {
    const card = `
        <div class="bg-gray-800 border border-gray-700 rounded-xl p-5 animate-pulse">
            <div class="flex justify-between mb-3">
                <div class="h-4 bg-gray-700 rounded w-2/3"></div>
                <div class="h-5 bg-gray-700 rounded-full w-16"></div>
            </div>
            <div class="flex gap-2 mb-3">
                <div class="h-4 bg-gray-700 rounded w-16"></div>
                <div class="h-4 bg-gray-700 rounded w-20"></div>
            </div>
            <div class="space-y-2 mb-4">
                <div class="h-3 bg-gray-700 rounded w-3/4"></div>
                <div class="h-3 bg-gray-700 rounded w-1/2"></div>
                <div class="h-3 bg-gray-700 rounded w-2/3"></div>
            </div>
            <div class="flex gap-2">
                <div class="h-7 bg-gray-700 rounded-lg w-20"></div>
                <div class="h-7 bg-gray-700 rounded-lg w-16"></div>
            </div>
        </div>`;
    document.getElementById(containerId).innerHTML = Array(count).fill(card).join('');
}

function renderEmpty(containerId, msg = 'No results. Adjust filters or import data.') {
    document.getElementById(containerId).innerHTML = `
        <div class="col-span-full flex flex-col items-center justify-center py-16 text-gray-600">
            <svg class="w-10 h-10 mb-3 opacity-30" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                    d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            <p class="text-sm">${escHtml(msg)}</p>
        </div>`;
}

function statusBadge(status) {
    const styles = {
        SAVED:     'bg-yellow-500/20 text-yellow-400 border border-yellow-500/30',
        APPLIED:   'bg-green-500/20  text-green-400  border border-green-500/30',
        REJECTED:  'bg-red-500/20    text-red-400    border border-red-500/30'
    };
    const icons = { SAVED: '🟡', APPLIED: '🟢', REJECTED: '🔴' };
    return `<span class="px-2 py-0.5 rounded-full text-xs font-medium whitespace-nowrap ${styles[status]}">${icons[status]} ${status}</span>`;
}

function actionBtn(label, onclick, active) {
    const base = 'px-3 py-1.5 text-xs rounded-lg font-medium transition-colors cursor-pointer';
    const activeClass = active ? 'opacity-60 cursor-default ' : 'hover:brightness-110 ';
    const color = label.includes('Apply') || label.includes('Save')
        ? (active ? 'bg-green-700 text-white ' : 'bg-green-700/30 text-green-400 hover:bg-green-700 hover:text-white ')
        : label.includes('Reject')
            ? (active ? 'bg-red-700 text-white ' : 'bg-red-700/30 text-red-400 hover:bg-red-700 hover:text-white ')
            : 'bg-gray-700 text-gray-400 hover:bg-gray-600 ';
    return `<button class="${base} ${activeClass}${color}" onclick="${onclick}">${label}</button>`;
}

// Import modal
let _currentModule = null;

function openImportModal(module) {
    _currentModule = module;
    document.getElementById('import-json-input').value = '';
    document.getElementById('import-submit-btn').disabled = false;
    document.getElementById('import-submit-btn').textContent = 'Import';
    document.getElementById('import-modal').classList.remove('hidden');
    setTimeout(() => document.getElementById('import-json-input').focus(), 100);
}

function closeImportModal() {
    document.getElementById('import-modal').classList.add('hidden');
    _currentModule = null;
}

async function submitImport() {
    const json = document.getElementById('import-json-input').value.trim();
    if (!json) { showToast('Paste JSON first', true); return; }
    try { JSON.parse(json); } catch { showToast('Invalid JSON — check format', true); return; }

    const module = _currentModule;
    const btn = document.getElementById('import-submit-btn');
    btn.disabled = true;
    btn.textContent = 'Importing…';

    try {
        const result = await apiFetch(`/api/${module}/import`, {
            method: 'POST',
            body: JSON.stringify({ json })
        });
        showToast(`✓ ${result.inserted} imported, ${result.skipped} skipped (total ${result.total})`);
        closeImportModal();
        _triggerFetch(module);
    } catch (e) {
        showToast(e.message, true);
        btn.disabled = false;
        btn.textContent = 'Import';
    }
}

function _triggerFetch(module) {
    const map = {
        'events':        fetchEvents,
        'jobs':          fetchJobs,
        'course-offers': fetchCourseOffers,
        'ai-offers':     fetchAIOffers,
        'marathons':     fetchMarathons
    };
    if (map[module]) map[module]();
}

// Tab switching
function switchTab(tab) {
    document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => {
        b.classList.remove('border-indigo-500', 'text-indigo-400');
        b.classList.add('border-transparent', 'text-gray-400');
    });
    document.getElementById(`panel-${tab}`).classList.add('active');
    const btn = document.getElementById(`tab-${tab}`);
    btn.classList.add('border-indigo-500', 'text-indigo-400');
    btn.classList.remove('border-transparent', 'text-gray-400');
}

// Close modal on Escape
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeImportModal();
});
