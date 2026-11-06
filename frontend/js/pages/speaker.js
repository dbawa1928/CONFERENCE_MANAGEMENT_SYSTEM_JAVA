/**
 * Speaker Dashboard: Assigned Sessions, Speaking Schedule & Profile
 */

import { api } from '../api.js';
import { auth } from '../auth.js';

export const speakerPages = {
    renderDashboard: async (container) => {
        const user = auth.getCurrentUser();

        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">Speaker Portal: ${user.name}</h1>
                    <p class="page-subtitle">Your speaking engagements, session schedule, and academic biography</p>
                </div>
            </div>

            <div class="tabs">
                <button class="tab-btn active" onclick="window.speakerPages.switchTab('sessions')">🗓️ Assigned Sessions</button>
                <button class="tab-btn" onclick="window.speakerPages.switchTab('profile')">👤 Speaker Profile</button>
            </div>

            <div id="speaker-tab-content">
                <p style="text-align: center; padding: 3rem; color: var(--text-muted);">Loading speaker schedule...</p>
            </div>
        `;

        await speakerPages.switchTab('sessions');
    },

    switchTab: async (tabName) => {
        const tabs = document.querySelectorAll('.tab-btn');
        tabs.forEach(t => t.classList.remove('active'));
        event?.target?.classList.add('active');

        const content = document.getElementById('speaker-tab-content');
        if (!content) return;

        if (tabName === 'sessions') {
            await speakerPages.renderSessionsTab(content);
        } else if (tabName === 'profile') {
            await speakerPages.renderProfileTab(content);
        }
    },

    renderSessionsTab: async (container) => {
        const user = auth.getCurrentUser();

        try {
            // First find speaker entity for current user
            const spkResp = await api.get(`/speakers/user/${user.id}`).catch(() => null);
            if (!spkResp || !spkResp.data) {
                container.innerHTML = `
                    <div class="card" style="text-align: center; padding: 3rem 1.5rem;">
                        <h4 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 0.5rem;">No Speaker Profile Active</h4>
                        <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">
                            Your speaker profile has not been initialized yet. Click below to configure your expertise and organization.
                        </p>
                        <button class="btn btn-primary" onclick="window.speakerPages.switchTab('profile')">
                            Configure Profile
                        </button>
                    </div>
                `;
                return;
            }

            const speaker = spkResp.data;
            const sessionsResp = await api.get(`/sessions/speaker/${speaker.id}`);
            const sessions = sessionsResp.data || [];

            if (sessions.length === 0) {
                container.innerHTML = `<p style="text-align: center; padding: 3rem; color: var(--text-muted);">No sessions currently assigned to you.</p>`;
                return;
            }

            container.innerHTML = `
                <div style="display: flex; flex-direction: column; gap: 1rem;">
                    ${sessions.map(s => `
                        <div class="card" style="border-left: 4px solid var(--primary);">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.5rem;">
                                <span class="badge badge-primary">${s.conferenceTitle}</span>
                                <span class="badge badge-secondary">${s.hall}</span>
                            </div>
                            <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 0.5rem;">
                                ${s.title}
                            </h3>
                            <p style="font-size: 0.9rem; color: var(--text-secondary); margin-bottom: 1rem;">
                                ${s.description || 'Keynote presentation.'}
                            </p>
                            <div style="font-size: 0.85rem; color: var(--text-muted); display: flex; gap: 1.5rem;">
                                <span>📅 <strong>Date:</strong> ${s.sessionDate}</span>
                                <span>🕒 <strong>Time:</strong> ${s.startTime.substring(0, 5)} - ${s.endTime.substring(0, 5)}</span>
                                <span>👥 <strong>Capacity:</strong> ${s.maxCapacity || 'Unlimited'} attendees</span>
                            </div>
                        </div>
                    `).join('')}
                </div>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    renderProfileTab: async (container) => {
        const user = auth.getCurrentUser();

        let speaker = null;
        try {
            const resp = await api.get(`/speakers/user/${user.id}`);
            speaker = resp.data;
        } catch (e) {
            // Profile may not exist yet
        }

        container.innerHTML = `
            <div class="card" style="max-width: 650px; margin: 0 auto;">
                <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1.25rem;">Speaker Profile</h3>
                <form id="speaker-profile-form" onsubmit="window.speakerPages.handleSaveProfile(event, ${speaker ? speaker.id : null})">
                    <div class="form-group">
                        <label>Organization / University *</label>
                        <input type="text" name="organization" class="form-control" required value="${speaker ? speaker.organization : ''}" placeholder="e.g. MIT CSAIL">
                    </div>
                    <div class="form-group">
                        <label>Designation *</label>
                        <input type="text" name="designation" class="form-control" required value="${speaker ? speaker.designation : ''}" placeholder="e.g. Lead Research Scientist">
                    </div>
                    <div class="form-group">
                        <label>Area of Expertise *</label>
                        <input type="text" name="expertise" class="form-control" required value="${speaker ? speaker.expertise : ''}" placeholder="e.g. Machine Learning, Distributed Algorithms">
                    </div>
                    <div class="form-group">
                        <label>Phone Number</label>
                        <input type="text" name="phone" class="form-control" value="${speaker ? (speaker.phone || '') : ''}" placeholder="+1 555-0123">
                    </div>
                    <div class="form-group">
                        <label>Biography</label>
                        <textarea name="biography" class="form-control" rows="4" placeholder="Your academic and professional bio...">${speaker ? (speaker.biography || '') : ''}</textarea>
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%;">
                        💾 Save Speaker Profile to MySQL
                    </button>
                </form>
            </div>
        `;
    },

    handleSaveProfile: async (e, speakerId) => {
        e.preventDefault();
        const user = auth.getCurrentUser();
        const formData = new FormData(e.target);

        const payload = {
            userId: user.id,
            organization: formData.get('organization'),
            designation: formData.get('designation'),
            expertise: formData.get('expertise'),
            phone: formData.get('phone'),
            biography: formData.get('biography')
        };

        try {
            if (speakerId) {
                await api.put(`/speakers/${speakerId}`, payload);
            } else {
                await api.post('/speakers', payload);
            }
            window.toast.show('Speaker profile saved successfully in MySQL!', 'success');
            await speakerPages.switchTab('sessions');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    }
};

window.speakerPages = speakerPages;

