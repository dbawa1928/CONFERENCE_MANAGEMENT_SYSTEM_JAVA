/**
 * Author Dashboard: Submit Research Papers, Peer Review Status Tracker, Reviewer Feedback
 */

import { api } from '../api.js';
import { auth } from '../auth.js';

export const authorPages = {
    renderDashboard: async (container) => {
        const user = auth.getCurrentUser();

        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">Author Portal: ${user.name}</h1>
                    <p class="page-subtitle">Submit academic papers, track peer-review progress, and view reviewer feedback</p>
                </div>
                <button class="btn btn-primary btn-sm" onclick="window.authorPages.showSubmitPaperModal()">
                    + Submit Research Paper
                </button>
            </div>

            <!-- Author Submissions Table -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Your Research Submissions</h3>
                </div>
                <div id="author-papers-container" class="table-responsive">
                    <p style="padding: 2.5rem; text-align: center; color: var(--text-muted);">Loading submitted papers from MySQL...</p>
                </div>
            </div>
        `;

        await authorPages.loadMyPapers();
    },

    loadMyPapers: async () => {
        const container = document.getElementById('author-papers-container');
        if (!container) return;

        try {
            const resp = await api.get('/papers/my');
            const papers = resp.data || [];

            if (papers.length === 0) {
                container.innerHTML = `
                    <div style="text-align: center; padding: 3rem 1.5rem;">
                        <div style="font-size: 3rem; margin-bottom: 1rem;">📄</div>
                        <h4 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 0.5rem;">No Papers Submitted Yet</h4>
                        <p style="color: var(--text-secondary); max-width: 440px; margin: 0 auto 1.25rem;">
                            Submit your research abstract and manuscript to an upcoming conference for double-blind peer review.
                        </p>
                        <button class="btn btn-primary" onclick="window.authorPages.showSubmitPaperModal()">
                            Submit Your First Paper
                        </button>
                    </div>
                `;
                return;
            }

            container.innerHTML = `
                <table class="table">
                    <thead>
                        <tr>
                            <th>Paper Title</th>
                            <th>Conference</th>
                            <th>Category & Keywords</th>
                            <th>Status Flow</th>
                            <th>Review Feedback</th>
                            <th>Manuscript</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${papers.map(p => {
                            let badgeClass = 'badge-secondary';
                            if (p.status === 'ACCEPTED') badgeClass = 'badge-success';
                            if (p.status === 'REJECTED') badgeClass = 'badge-danger';
                            if (p.status === 'UNDER_REVIEW') badgeClass = 'badge-warning';

                            return `
                                <tr>
                                    <td>
                                        <strong>${p.title}</strong><br>
                                        <small style="color:var(--text-muted);">Submitted: ${p.submittedAt.substring(0, 10)}</small>
                                    </td>
                                    <td>${p.conferenceTitle}</td>
                                    <td>
                                        <span class="badge badge-primary">${p.category}</span><br>
                                        <small style="color:var(--text-muted);">${p.keywords}</small>
                                    </td>
                                    <td>
                                        <span class="badge ${badgeClass}">${p.status}</span>
                                    </td>
                                    <td>
                                        ${p.reviewCount > 0 ? `
                                            <button class="btn btn-secondary btn-sm" onclick="window.authorPages.showFeedbackModal(${p.id})">
                                                ⭐ Feedback (${p.averageScore}/10)
                                            </button>
                                        ` : `
                                            <span style="font-size: 0.85rem; color: var(--text-muted);">Under Peer Review</span>
                                        `}
                                    </td>
                                    <td>
                                        ${p.filePath ? `
                                            <a href="http://localhost:8080/api/papers/${p.id}/download" target="_blank" class="btn btn-outline btn-sm">
                                                📥 PDF
                                            </a>
                                        ` : '<span style="color:var(--text-muted)">No File</span>'}
                                    </td>
                                </tr>
                            `;
                        }).join('')}
                    </tbody>
                </table>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    showSubmitPaperModal: async () => {
        try {
            const confResp = await api.get('/conferences');
            const confs = confResp.data || [];

            if (confs.length === 0) {
                window.toast.show('No active conferences available for paper submission.', 'warning');
                return;
            }

            window.modal.show({
                title: 'Submit Research Paper',
                size: 'modal-lg',
                content: `
                    <form id="submit-paper-form" onsubmit="window.authorPages.handleSubmitPaper(event)">
                        <div class="form-group">
                            <label>Select Conference *</label>
                            <select name="conferenceId" class="form-control" required>
                                ${confs.map(c => `<option value="${c.id}">${c.title} (${c.category})</option>`).join('')}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Paper Title *</label>
                            <input type="text" name="title" class="form-control" required placeholder="Full academic paper title">
                        </div>
                        <div class="form-group">
                            <label>Abstract *</label>
                            <textarea name="abstractText" class="form-control" rows="4" required placeholder="Comprehensive paper abstract summarizing problem, methodology, and results..."></textarea>
                        </div>
                        <div class="grid-2">
                            <div class="form-group">
                                <label>Research Category *</label>
                                <input type="text" name="category" class="form-control" required placeholder="e.g. Machine Learning, Cloud Systems">
                            </div>
                            <div class="form-group">
                                <label>Keywords *</label>
                                <input type="text" name="keywords" class="form-control" required placeholder="Comma separated, e.g. AI, RL, Healthcare">
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Manuscript PDF File</label>
                            <input type="file" name="file" class="form-control" accept=".pdf,.doc,.docx">
                            <small style="color: var(--text-muted); display: block; margin-top: 0.25rem;">
                                Files are stored locally in <code>backend/uploads/papers/</code>
                            </small>
                        </div>
                        <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                            <button type="submit" class="btn btn-primary">Submit to Conference</button>
                        </div>
                    </form>
                `
            });
        } catch (e) {
            window.toast.show('Failed to prepare submission form: ' + e.message, 'error');
        }
    },

    handleSubmitPaper: async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        try {
            await api.post('/papers', formData);
            window.modal.close();
            window.toast.show('Research paper submitted successfully and stored in MySQL!', 'success');
            await authorPages.loadMyPapers();
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    showFeedbackModal: async (paperId) => {
        try {
            const resp = await api.get(`/reviews/paper/${paperId}`);
            const reviews = resp.data || [];

            if (reviews.length === 0) {
                window.toast.show('No reviews have been completed yet for this paper.', 'info');
                return;
            }

            window.modal.show({
                title: '⭐ Peer Reviewer Feedback',
                content: `
                    <div style="display: flex; flex-direction: column; gap: 1.5rem;">
                        ${reviews.map((r, idx) => `
                            <div style="border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.25rem; background: #f8fafc;">
                                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.75rem;">
                                    <strong>Reviewer #${idx + 1} Evaluation</strong>
                                    <span class="badge ${r.recommendation === 'ACCEPT' ? 'badge-success' : 'badge-warning'}">${r.recommendation}</span>
                                </div>

                                <div class="grid-2" style="margin-bottom: 1rem; font-size: 0.85rem; background: white; padding: 0.75rem; border-radius: var(--radius-sm);">
                                    <div>Originality: <strong>${r.originalityScore} / 10</strong></div>
                                    <div>Technical Quality: <strong>${r.technicalQualityScore} / 10</strong></div>
                                    <div>Relevance: <strong>${r.relevanceScore} / 10</strong></div>
                                    <div>Presentation: <strong>${r.presentationScore} / 10</strong></div>
                                </div>

                                <div style="font-size: 1.05rem; font-weight: 800; color: var(--primary); margin-bottom: 0.75rem;">
                                    Overall Score: ${r.overallScore} / 10
                                </div>

                                <div>
                                    <span style="font-size: 0.8rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase;">Reviewer Comments:</span>
                                    <p style="font-size: 0.9rem; color: var(--text-primary); margin-top: 0.25rem; line-height: 1.5;">
                                        ${r.comments || 'No comments provided.'}
                                    </p>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                `,
                footer: `<button class="btn btn-secondary" onclick="window.modal.close()">Close</button>`
            });
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    }
};

window.authorPages = authorPages;

