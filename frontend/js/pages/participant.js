/**
 * Participant Dashboard: Registrations, QR Code Pass, Schedule, Certificates, Notifications
 */

import { api } from '../api.js';
import { auth } from '../auth.js';

export const participantPages = {
    renderDashboard: async (container) => {
        const user = auth.getCurrentUser();

        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">Welcome, ${user.name}!</h1>
                    <p class="page-subtitle">Participant Dashboard & Conference Passes</p>
                </div>
                <button class="btn btn-primary btn-sm" onclick="window.router.navigate('/conferences')">
                    + Browse More Conferences
                </button>
            </div>

            <!-- Participant Tabs -->
            <div class="tabs">
                <button class="tab-btn active" onclick="window.participantPages.switchTab('registrations')">🎟️ My Passes & QR Codes</button>
                <button class="tab-btn" onclick="window.participantPages.switchTab('schedule')">🗓️ My Schedule</button>
                <button class="tab-btn" onclick="window.participantPages.switchTab('certificates')">🏆 My Certificates</button>
                <button class="tab-btn" onclick="window.participantPages.switchTab('notifications')">🔔 Notifications</button>
                <button class="tab-btn" onclick="window.participantPages.switchTab('profile')">👤 My Profile</button>
            </div>

            <div id="participant-tab-content">
                <p style="text-align: center; padding: 3rem; color: var(--text-muted);">Loading your data from MySQL...</p>
            </div>
        `;

        await participantPages.switchTab('registrations');
    },

    switchTab: async (tabName) => {
        const tabs = document.querySelectorAll('.tab-btn');
        tabs.forEach(t => t.classList.remove('active'));
        event?.target?.classList.add('active');

        const content = document.getElementById('participant-tab-content');
        if (!content) return;

        if (tabName === 'registrations') {
            await participantPages.renderRegistrationsTab(content);
        } else if (tabName === 'schedule') {
            await participantPages.renderScheduleTab(content);
        } else if (tabName === 'certificates') {
            await participantPages.renderCertificatesTab(content);
        } else if (tabName === 'notifications') {
            await participantPages.renderNotificationsTab(content);
        } else if (tabName === 'profile') {
            participantPages.renderProfileTab(content);
        }
    },

    // --------------------------------------------------------------------------
    // Tab 1: My Registrations & QR Digital Pass
    // --------------------------------------------------------------------------
    renderRegistrationsTab: async (container) => {
        try {
            const resp = await api.get('/registrations/my');
            const registrations = resp.data || [];

            if (registrations.length === 0) {
                container.innerHTML = `
                    <div class="card" style="text-align: center; padding: 3.5rem 1.5rem;">
                        <div style="font-size: 3rem; margin-bottom: 1rem;">🎫</div>
                        <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 0.5rem;">No Registrations Yet</h3>
                        <p style="color: var(--text-secondary); max-width: 480px; margin: 0 auto 1.5rem;">
                            You haven't registered for any conference yet. Browse our upcoming academic summits to secure your seat.
                        </p>
                        <button class="btn btn-primary" onclick="window.router.navigate('/conferences')">
                            Explore Conferences
                        </button>
                    </div>
                `;
                return;
            }

            container.innerHTML = `
                <div class="grid-2">
                    ${registrations.map(reg => `
                        <div class="card" style="border-left: 5px solid ${reg.attendanceStatus ? 'var(--success)' : 'var(--primary)'};">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.75rem;">
                                <span class="badge ${reg.status === 'REGISTERED' ? 'badge-primary' : 'badge-secondary'}">${reg.status}</span>
                                <span class="badge ${reg.attendanceStatus ? 'badge-success' : 'badge-warning'}">
                                    ${reg.attendanceStatus ? '✅ Attended' : '⏳ Awaiting Check-In'}
                                </span>
                            </div>
                            <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">
                                ${reg.conference.title}
                            </h3>
                            <div style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 0.35rem;">
                                📍 <strong>Venue:</strong> ${reg.conference.venue}
                            </div>
                            <div style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 1rem;">
                                📅 <strong>Dates:</strong> ${reg.conference.startDate} to ${reg.conference.endDate}
                            </div>

                            <div style="display: flex; gap: 0.75rem; align-items: center; justify-content: space-between; border-top: 1px solid var(--border-color); padding-top: 1rem;">
                                <button class="btn btn-primary btn-sm" onclick="window.participantPages.showTicketModal(${reg.id})">
                                    🎟️ View QR Pass
                                </button>
                                ${reg.attendanceStatus ? `
                                    <button class="btn btn-success btn-sm" onclick="window.participantPages.generateCertificate(${reg.conference.id})">
                                        🏆 Get Certificate
                                    </button>
                                ` : ''}
                                ${reg.status === 'REGISTERED' && !reg.attendanceStatus ? `
                                    <button class="btn btn-outline btn-sm" style="color:var(--danger); border-color:var(--danger);" onclick="window.participantPages.cancelRegistration(${reg.id})">
                                        Cancel
                                    </button>
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

    showTicketModal: async (regId) => {
        try {
            const resp = await api.get(`/registrations/${regId}`);
            const reg = resp.data;

            window.modal.show({
                title: '🎟️ Official Digital Conference Pass',
                content: `
                    <div style="text-align: center;">
                        <h3 style="font-size: 1.25rem; font-weight: 800; color: var(--primary); margin-bottom: 0.25rem;">
                            ${reg.conference.title}
                        </h3>
                        <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 1.25rem;">
                            ConfSync Verified Attendee Pass
                        </p>

                        <div style="display: inline-block; padding: 1rem; background: white; border: 2px dashed var(--border-color); border-radius: var(--radius-md); margin-bottom: 1.25rem; box-shadow: var(--shadow-sm);">
                            <img src="${reg.qrCode}" alt="Attendance QR Code" style="width: 220px; height: 220px; display: block;">
                        </div>

                        <div style="background-color: #f8fafc; padding: 1rem; border-radius: var(--radius-md); text-align: left; margin-bottom: 1rem; font-size: 0.875rem;">
                            <div style="margin-bottom: 0.35rem;">👤 <strong>Attendee:</strong> ${reg.participant.name}</div>
                            <div style="margin-bottom: 0.35rem;">✉️ <strong>Email:</strong> ${reg.participant.email}</div>
                            <div style="margin-bottom: 0.35rem;">📍 <strong>Venue:</strong> ${reg.conference.venue}</div>
                            <div>🕒 <strong>Registered On:</strong> ${reg.registrationDate.replace('T', ' ').substring(0, 16)}</div>
                        </div>

                        <p style="font-size: 0.8rem; color: var(--text-muted);">
                            Present this QR code at the registration desk. Once scanned by the organizer, your attendance will be logged in MySQL.
                        </p>
                    </div>
                `,
                footer: `
                    <button class="btn btn-secondary" onclick="window.modal.close()">Close</button>
                    <button class="btn btn-primary" onclick="window.print()">🖨️ Print Pass</button>
                `
            });
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    cancelRegistration: async (regId) => {
        if (!confirm('Are you sure you want to cancel your conference registration?')) return;
        try {
            await api.delete(`/registrations/${regId}`);
            window.toast.show('Registration cancelled successfully', 'success');
            await participantPages.switchTab('registrations');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    generateCertificate: async (conferenceId) => {
        const user = auth.getCurrentUser();
        try {
            const resp = await api.post('/certificates/generate', {
                conferenceId,
                participantId: user.id
            });
            window.toast.show('Certificate ready! Opening certificates tab...', 'success');
            await participantPages.switchTab('certificates');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    // --------------------------------------------------------------------------
    // Tab 2: My Conference Schedule
    // --------------------------------------------------------------------------
    renderScheduleTab: async (container) => {
        try {
            const regResp = await api.get('/registrations/my');
            const registrations = (regResp.data || []).filter(r => r.status === 'REGISTERED');

            if (registrations.length === 0) {
                container.innerHTML = `<p style="text-align: center; padding: 3rem; color: var(--text-muted);">Register for conferences to build your personal session schedule.</p>`;
                return;
            }

            // Fetch sessions for all registered conferences
            const sessionPromises = registrations.map(r => api.get(`/sessions/conference/${r.conference.id}`));
            const sessionResponses = await Promise.all(sessionPromises);

            let allSessions = [];
            sessionResponses.forEach(r => {
                if (r.data) allSessions = allSessions.concat(r.data);
            });

            // Sort by date and start time
            allSessions.sort((a, b) => (a.sessionDate + a.startTime).localeCompare(b.sessionDate + b.startTime));

            if (allSessions.length === 0) {
                container.innerHTML = `<p style="text-align: center; padding: 3rem; color: var(--text-muted);">No sessions have been scheduled yet for your conferences.</p>`;
                return;
            }

            container.innerHTML = `
                <div style="display: flex; flex-direction: column; gap: 1rem;">
                    ${allSessions.map(s => `
                        <div class="card" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                            <div>
                                <div style="font-size: 0.8rem; color: var(--primary); font-weight: 700; text-transform: uppercase;">
                                    ${s.conferenceTitle}
                                </div>
                                <h3 style="font-size: 1.15rem; font-weight: 700; margin: 0.25rem 0;">
                                    ${s.title}
                                </h3>
                                <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 0.25rem;">
                                    ${s.description || ''}
                                </p>
                                <div style="font-size: 0.825rem; color: var(--text-muted); display: flex; gap: 1rem;">
                                    <span>📍 <strong>Hall:</strong> ${s.hall}</span>
                                    ${s.speaker ? `<span>🎤 <strong>Speaker:</strong> ${s.speaker.user.name} (${s.speaker.organization})</span>` : ''}
                                </div>
                            </div>
                            <div style="background-color: var(--primary-light); color: var(--primary); padding: 0.75rem 1.25rem; border-radius: var(--radius-md); text-align: center; font-weight: 700;">
                                <div style="font-size: 0.95rem;">${s.sessionDate}</div>
                                <div style="font-size: 0.825rem;">${s.startTime.substring(0, 5)} - ${s.endTime.substring(0, 5)}</div>
                            </div>
                        </div>
                    `).join('')}
                </div>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    // --------------------------------------------------------------------------
    // Tab 3: My Certificates
    // --------------------------------------------------------------------------
    renderCertificatesTab: async (container) => {
        try {
            const resp = await api.get('/certificates/my');
            const certificates = resp.data || [];

            if (certificates.length === 0) {
                container.innerHTML = `
                    <div class="card" style="text-align: center; padding: 3rem 1.5rem;">
                        <div style="font-size: 3rem; margin-bottom: 1rem;">🏆</div>
                        <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 0.5rem;">No Certificates Earned Yet</h3>
                        <p style="color: var(--text-secondary); max-width: 460px; margin: 0 auto;">
                            Participation certificates are automatically unlocked and generated once your attendance is verified by the conference organizer at the venue.
                        </p>
                    </div>
                `;
                return;
            }

            container.innerHTML = `
                <div class="grid-2">
                    ${certificates.map(cert => `
                        <div class="card" style="background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%); border: 2px solid #e2e8f0;">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1rem;">
                                <span class="badge badge-success">VERIFIED CERTIFICATE</span>
                                <span style="font-size: 0.8rem; color: var(--text-muted); font-family: monospace;">${cert.certificateNumber}</span>
                            </div>
                            <h3 style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.5rem;">
                                ${cert.conferenceTitle}
                            </h3>
                            <p style="font-size: 0.875rem; color: var(--text-secondary); margin-bottom: 1.25rem;">
                                Issued to: <strong>${cert.participantName}</strong><br>
                                Verification Code: <code>${cert.verificationCode}</code>
                            </p>
                            <a href="http://localhost:8080/api/certificates/${cert.id}/download" target="_blank" class="btn btn-primary btn-sm" style="width: 100%;">
                                📥 Download Official PDF Certificate
                            </a>
                        </div>
                    `).join('')}
                </div>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    // --------------------------------------------------------------------------
    // Tab 4: Notifications
    // --------------------------------------------------------------------------
    renderNotificationsTab: async (container) => {
        try {
            const resp = await api.get('/notifications');
            const notifications = resp.data || [];

            container.innerHTML = `
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                    <h3 style="font-size: 1.15rem; font-weight: 700;">In-App Activity Alerts</h3>
                    ${notifications.length > 0 ? `
                        <button class="btn btn-secondary btn-sm" onclick="window.participantPages.markAllNotificationsRead()">
                            Mark All Read
                        </button>
                    ` : ''}
                </div>

                ${notifications.length === 0 ? `
                    <p style="text-align: center; padding: 3rem; color: var(--text-muted);">You have no notifications at this time.</p>
                ` : `
                    <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                        ${notifications.map(n => `
                            <div class="card" style="padding: 1rem 1.25rem; border-left: 4px solid ${n.isRead ? 'var(--border-color)' : 'var(--primary)'}; background: ${n.isRead ? '#ffffff' : '#f0f7ff'};">
                                <div style="display: flex; justify-content: space-between; align-items: flex-start;">
                                    <strong style="color: var(--text-primary); font-size: 0.95rem;">${n.title}</strong>
                                    <small style="color: var(--text-muted);">${n.createdAt.replace('T', ' ').substring(0, 16)}</small>
                                </div>
                                <p style="font-size: 0.875rem; color: var(--text-secondary); margin-top: 0.25rem;">
                                    ${n.message}
                                </p>
                            </div>
                        `).join('')}
                    </div>
                `}
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    markAllNotificationsRead: async () => {
        try {
            await api.put('/notifications/read-all');
            window.toast.show('All notifications marked as read', 'success');
            await participantPages.switchTab('notifications');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    // --------------------------------------------------------------------------
    // Tab 5: Profile
    // --------------------------------------------------------------------------
    renderProfileTab: (container) => {
        const user = auth.getCurrentUser();
        container.innerHTML = `
            <div class="card" style="max-width: 600px; margin: 0 auto; padding: 2rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1.5rem;">User Profile Details</h3>
                <div style="display: flex; flex-direction: column; gap: 1rem; font-size: 0.95rem;">
                    <div>
                        <span style="color: var(--text-muted); display: block; font-size: 0.8rem;">FULL NAME</span>
                        <strong>${user.name}</strong>
                    </div>
                    <div>
                        <span style="color: var(--text-muted); display: block; font-size: 0.8rem;">EMAIL ADDRESS</span>
                        <strong>${user.email}</strong>
                    </div>
                    <div>
                        <span style="color: var(--text-muted); display: block; font-size: 0.8rem;">ASSIGNED ROLE</span>
                        <span class="badge badge-primary">${user.role}</span>
                    </div>
                    <div>
                        <span style="color: var(--text-muted); display: block; font-size: 0.8rem;">ACCOUNT CREATED</span>
                        <strong>${user.createdAt ? user.createdAt.replace('T', ' ').substring(0, 16) : 'Verified'}</strong>
                    </div>
                </div>
            </div>
        `;
    }
};

window.participantPages = participantPages;

