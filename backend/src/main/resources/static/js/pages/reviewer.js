/**
 * Reviewer Dashboard: Assigned Research Papers & Scoring Modal
 */

import { api } from '../api.js';
import { auth } from '../auth.js';

export const reviewerPages = {
    renderDashboard: async (container) => {
        const user = auth.getCurrentUser();

        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">Reviewer Portal: ${user.name}</h1>
                    <p class="page-subtitle">Double-blind peer review assignments and evaluation scoring</p>
                </div>
            </div>

            <!-- Reviewer Tabs -->
            <div class="tabs">
                <button class="tab-btn active" onclick="window.reviewerPages.switchTab('assigned')">📝 Assigned Papers</button>
                <button class="tab-btn" onclick="window.reviewerPages.switchTab('completed')">✅ Completed Evaluations</button>
            </div>

            <div id="reviewer-tab-content">
                <p style="text-align: center; padding: 3rem; color: var(--text-muted);">Loading assignments from MySQL...</p>
            </div>
        `;

        await reviewerPages.switchTab('assigned');
    },

    switchTab: async (tabName) => {
        const tabs = document.querySelectorAll('.tab-btn');
        tabs.forEach(t => t.classList.remove('active'));
        event?.target?.classList.add('active');

        const content = document.getElementById('reviewer-tab-content');
        if (!content) return;

        if (tabName === 'assigned') {
            await reviewerPages.renderAssignedTab(content);
        } else if (tabName === 'completed') {
            await reviewerPages.renderCompletedTab(content);
        }
    },

    renderAssignedTab: async (container) => {
        try {
            const resp = await api.get('/reviews/assigned');
            const assignments = resp.data || [];

            if (assignments.length === 0) {
                container.innerHTML = `
                    <div class="card" style="text-align: center; padding: 3.5rem 1.5rem;">
                        <div style="font-size: 3rem; margin-bottom: 1rem;">🔍</div>
                        <h4 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 0.5rem;">No Pending Assignments</h4>
                        <p style="color: var(--text-secondary); max-width: 440px; margin: 0 auto;">
                            You currently have no papers pending evaluation. New assignments will appear here when allocated by the program committee.
                        </p>
                    </div>
                `;
                return;
            }

            container.innerHTML = `
                <div style="display: flex; flex-direction: column; gap: 1rem;">
                    ${assignments.map(a => `
                        <div class="card">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.5rem;">
                                <span class="badge ${a.status === 'COMPLETED' ? 'badge-success' : 'badge-warning'}">${a.status}</span>
                                <small style="color: var(--text-muted);">Assigned on: ${a.assignedAt.substring(0, 10)}</small>
                            </div>
                            <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 0.5rem;">
                                ${a.paper.title}
                            </h3>
                            <div style="margin-bottom: 0.75rem;">
                                <span class="badge badge-primary">${a.paper.category}</span>
                                <span style="font-size: 0.85rem; color: var(--text-muted); margin-left: 0.5rem;">Conference: <strong>${a.paper.conferenceTitle}</strong></span>
                            </div>
                            <p style="font-size: 0.9rem; color: var(--text-secondary); line-height: 1.6; margin-bottom: 1.25rem; background: #f8fafc; padding: 1rem; border-radius: var(--radius-sm);">
                                <strong>Abstract:</strong> ${a.paper.abstractText}
                            </p>
                            <div style="display: flex; gap: 0.75rem; align-items: center;">
                                <button class="btn btn-primary btn-sm" onclick="window.reviewerPages.showReviewModal(${a.paper.id}, '${a.paper.title.replace(/'/g, "\\'")}')">
                                    ⭐ ${a.status === 'COMPLETED' ? 'Edit Evaluation' : 'Evaluate Paper'}
                                </button>
                                ${a.paper.filePath ? `
                                    <a href="http://localhost:8080/api/papers/${a.paper.id}/download" target="_blank" class="btn btn-outline btn-sm">
                                        📥 Read Manuscript (PDF)
                                    </a>
                                ` : ''}
                            </div>
                        </div>
                    `).join('')}
                </div>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    showReviewModal: (paperId, paperTitle) => {
        window.modal.show({
            title: 'Peer Review: ' + paperTitle,
            size: 'modal-lg',
            content: `
                <form id="submit-review-form" onsubmit="window.reviewerPages.handleSubmitReview(event, ${paperId})">
                    <p style="font-size: 0.875rem; color: var(--text-secondary); margin-bottom: 1.25rem;">
                        Score each criterion from 1 (poor) to 10 (exceptional). The overall score is automatically computed as:
                        <code>(Originality + Technical + Relevance + Presentation) / 4</code>.
                    </p>

                    <div class="grid-2">
                        <div class="form-group">
                            <label>Originality (1-10): <strong id="val-orig">8</strong></label>
                            <input type="range" name="originalityScore" class="form-control" min="1" max="10" step="0.5" value="8"
                                   oninput="document.getElementById('val-orig').innerText = this.value; window.reviewerPages.updateAverageScore();">
                        </div>
                        <div class="form-group">
                            <label>Technical Quality (1-10): <strong id="val-tech">8</strong></label>
                            <input type="range" name="technicalQualityScore" class="form-control" min="1" max="10" step="0.5" value="8"
                                   oninput="document.getElementById('val-tech').innerText = this.value; window.reviewerPages.updateAverageScore();">
                        </div>
                        <div class="form-group">
                            <label>Relevance (1-10): <strong id="val-rel">8</strong></label>
                            <input type="range" name="relevanceScore" class="form-control" min="1" max="10" step="0.5" value="8"
                                   oninput="document.getElementById('val-rel').innerText = this.value; window.reviewerPages.updateAverageScore();">
                        </div>
                        <div class="form-group">
                            <label>Presentation (1-10): <strong id="val-pres">8</strong></label>
                            <input type="range" name="presentationScore" class="form-control" min="1" max="10" step="0.5" value="8"
                                   oninput="document.getElementById('val-pres').innerText = this.value; window.reviewerPages.updateAverageScore();">
                        </div>
                    </div>

                    <div style="background-color: var(--primary-light); padding: 1rem; border-radius: var(--radius-md); text-align: center; margin-bottom: 1.25rem;">
                        <span style="font-size: 0.9rem; color: var(--text-secondary); font-weight: 600;">CALCULATED OVERALL SCORE</span>
                        <div id="val-overall" style="font-size: 2rem; font-weight: 800; color: var(--primary);">8.0 / 10</div>
                    </div>

                    <div class="form-group">
                        <label>Recommendation *</label>
                        <select name="recommendation" class="form-control" required>
                            <option value="ACCEPT">ACCEPT</option>
                            <option value="REVISION_REQUIRED">REVISION REQUIRED</option>
                            <option value="REJECT">REJECT</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Constructive Comments & Feedback for Author</label>
                        <textarea name="comments" class="form-control" rows="4" placeholder="Detail the strengths, weaknesses, and potential improvements..."></textarea>
                    </div>

                    <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                        <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                        <button type="submit" class="btn btn-primary">Submit Evaluation</button>
                    </div>
                </form>
            `
        });
    },

    updateAverageScore: () => {
        const form = document.getElementById('submit-review-form');
        if (!form) return;

        const orig = parseFloat(form.elements['originalityScore'].value);
        const tech = parseFloat(form.elements['technicalQualityScore'].value);
        const rel = parseFloat(form.elements['relevanceScore'].value);
        const pres = parseFloat(form.elements['presentationScore'].value);

        const avg = (orig + tech + rel + pres) / 4.0;
        document.getElementById('val-overall').innerText = avg.toFixed(2) + ' / 10';
    },

    handleSubmitReview: async (e, paperId) => {
        e.preventDefault();
        const form = e.target;
        const payload = {
            originalityScore: parseFloat(form.elements['originalityScore'].value),
            technicalQualityScore: parseFloat(form.elements['technicalQualityScore'].value),
            relevanceScore: parseFloat(form.elements['relevanceScore'].value),
            presentationScore: parseFloat(form.elements['presentationScore'].value),
            recommendation: form.elements['recommendation'].value,
            comments: form.elements['comments'].value
        };

        try {
            await api.post(`/reviews/${paperId}`, payload);
            window.modal.close();
            window.toast.show('Peer review submitted and saved into MySQL!', 'success');
            await reviewerPages.switchTab('completed');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    renderCompletedTab: async (container) => {
        try {
            const resp = await api.get('/reviews/my');
            const reviews = resp.data || [];

            if (reviews.length === 0) {
                container.innerHTML = `<p style="text-align: center; padding: 3rem; color: var(--text-muted);">You have not completed any reviews yet.</p>`;
                return;
            }

            container.innerHTML = `
                <div style="display: flex; flex-direction: column; gap: 1rem;">
                    ${reviews.map(r => `
                        <div class="card">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.5rem;">
                                <strong>${r.paperTitle}</strong>
                                <span class="badge ${r.recommendation === 'ACCEPT' ? 'badge-success' : 'badge-warning'}">${r.recommendation}</span>
                            </div>
                            <div style="font-size: 0.95rem; font-weight: 700; color: var(--primary); margin-bottom: 0.5rem;">
                                Score: ${r.overallScore} / 10 (Orig: ${r.originalityScore}, Tech: ${r.technicalQualityScore}, Rel: ${r.relevanceScore}, Pres: ${r.presentationScore})
                            </div>
                            <p style="font-size: 0.875rem; color: var(--text-secondary); line-height: 1.5;">
                                "${r.comments || 'No comments'}"
                            </p>
                        </div>
                    `).join('')}
                </div>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    }
};

window.reviewerPages = reviewerPages;

