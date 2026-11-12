/**
 * Admin Dashboard & Management Modules
 */

import { api } from '../api.js';

export const adminPages = {
    renderDashboard: async (container) => {
        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">Admin Management Portal</h1>
                    <p class="page-subtitle">Real-time conference operations, analytics, and system oversight</p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <button class="btn btn-primary btn-sm" onclick="window.adminPages.showCreateConferenceModal()">
                        + New Conference
                    </button>
                    <button class="btn btn-secondary btn-sm" onclick="window.adminPages.showAttendanceScanner()">
                        🎟️ QR Check-In
                    </button>
                </div>
            </div>

            <!-- Management Navigation Tabs -->
            <div class="tabs">
                <button class="tab-btn active" onclick="window.adminPages.switchTab('overview')">📊 Overview & Analytics</button>
                <button class="tab-btn" onclick="window.adminPages.switchTab('conferences')">📅 Conferences</button>
                <button class="tab-btn" onclick="window.adminPages.switchTab('sessions')">🗓️ Sessions</button>
                <button class="tab-btn" onclick="window.adminPages.switchTab('speakers')">🎤 Speakers</button>
                <button class="tab-btn" onclick="window.adminPages.switchTab('papers')">📄 Research Papers</button>
                <button class="tab-btn" onclick="window.adminPages.switchTab('users')">👥 Users & Roles</button>
                <button class="tab-btn" onclick="window.adminPages.switchTab('certificates')">🏆 Certificates</button>
            </div>

            <div id="admin-tab-content">
                <div style="text-align: center; padding: 3rem; color: var(--text-muted);">
                    Loading analytics from MySQL...
                </div>
            </div>
        `;

        await adminPages.switchTab('overview');
    },

    switchTab: async (tabName) => {
        const tabs = document.querySelectorAll('.tab-btn');
        tabs.forEach(t => t.classList.remove('active'));
        event?.target?.classList.add('active');

        const content = document.getElementById('admin-tab-content');
        if (!content) return;

        if (tabName === 'overview') {
            await adminPages.renderOverviewTab(content);
        } else if (tabName === 'conferences') {
            await adminPages.renderConferencesTab(content);
        } else if (tabName === 'sessions') {
            await adminPages.renderSessionsTab(content);
        } else if (tabName === 'speakers') {
            await adminPages.renderSpeakersTab(content);
        } else if (tabName === 'papers') {
            await adminPages.renderPapersTab(content);
        } else if (tabName === 'users') {
            await adminPages.renderUsersTab(content);
        } else if (tabName === 'certificates') {
            await adminPages.renderCertificatesTab(content);
        }
    },

    // --------------------------------------------------------------------------
    // Tab 1: Overview & Analytics
    // --------------------------------------------------------------------------
    renderOverviewTab: async (container) => {
        try {
            const resp = await api.get('/dashboard/stats');
            const stats = resp.data || {};

            container.innerHTML = `
                <div class="grid-4" style="margin-bottom: 2rem;">
                    <div class="stat-card">
                        <div class="stat-icon blue">👥</div>
                        <div>
                            <div class="stat-value">${stats.totalUsers || 0}</div>
                            <div class="stat-label">Total Users</div>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon green">📅</div>
                        <div>
                            <div class="stat-value">${stats.totalConferences || 0}</div>
                            <div class="stat-label">Conferences</div>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon orange">📄</div>
                        <div>
                            <div class="stat-value">${stats.totalPapers || 0}</div>
                            <div class="stat-label">Submitted Papers</div>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon purple">🎟️</div>
                        <div>
                            <div class="stat-value">${stats.totalAttendance || 0}</div>
                            <div class="stat-label">Verified Attendance</div>
                        </div>
                    </div>
                </div>

                <div class="grid-3" style="margin-bottom: 2rem;">
                    <div class="card">
                        <h3 class="card-title">Research Papers Breakdown</h3>
                        <div style="margin-top: 1rem; display: flex; flex-direction: column; gap: 0.75rem;">
                            <div style="display: flex; justify-content: space-between;">
                                <span>Accepted Papers</span>
                                <strong class="badge badge-success">${stats.acceptedPapers || 0}</strong>
                            </div>
                            <div style="display: flex; justify-content: space-between;">
                                <span>Pending Reviews</span>
                                <strong class="badge badge-warning">${stats.pendingReviews || 0}</strong>
                            </div>
                            <div style="display: flex; justify-content: space-between;">
                                <span>Total Submissions</span>
                                <strong>${stats.totalPapers || 0}</strong>
                            </div>
                        </div>
                    </div>

                    <div class="card">
                        <h3 class="card-title">Conferences by Category</h3>
                        <div style="margin-top: 1rem; display: flex; flex-direction: column; gap: 0.75rem;">
                            ${Object.entries(stats.conferencesByCategory || {}).map(([cat, count]) => `
                                <div style="display: flex; justify-content: space-between;">
                                    <span>${cat}</span>
                                    <span class="badge badge-primary">${count}</span>
                                </div>
                            `).join('') || '<p style="color: var(--text-muted); font-size: 0.85rem;">No conference data yet.</p>'}
                        </div>
                    </div>

                    <div class="card">
                        <h3 class="card-title">Users by Role</h3>
                        <div style="margin-top: 1rem; display: flex; flex-direction: column; gap: 0.75rem;">
                            ${Object.entries(stats.usersByRole || {}).map(([role, count]) => `
                                <div style="display: flex; justify-content: space-between;">
                                    <span>${role}</span>
                                    <span class="badge badge-secondary">${count}</span>
                                </div>
                            `).join('') || '<p style="color: var(--text-muted); font-size: 0.85rem;">No user data yet.</p>'}
                        </div>
                    </div>
                </div>

                <div class="card" style="padding: 1.5rem; background: #f8fafc; border-left: 4px solid var(--primary);">
                    <h4 style="font-weight: 700; margin-bottom: 0.5rem; color: var(--primary);">
                        💾 MySQL Real-Time Persistence
                    </h4>
                    <p style="font-size: 0.9rem; color: var(--text-secondary); line-height: 1.6;">
                        All counts and categories above are computed via Java Streams and custom JPA repository queries
                        directly against your local MySQL database (<code>confsync_db</code>). Creating, editing, or deleting
                        records in any tab will instantly update these metrics.
                    </p>
                </div>
            `;
        } catch (e) {
            container.innerHTML = `<div style="text-align: center; padding: 3rem; color: var(--danger);">Failed to load overview: ${e.message}</div>`;
        }
    },

    // --------------------------------------------------------------------------
    // Tab 2: Manage Conferences
    // --------------------------------------------------------------------------
    renderConferencesTab: async (container) => {
        container.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700;">Conferences in MySQL</h3>
                <button class="btn btn-primary btn-sm" onclick="window.adminPages.showCreateConferenceModal()">
                    + Add Conference
                </button>
            </div>
            <div id="admin-conferences-table" class="table-responsive">
                <p style="padding: 2rem; text-align: center; color: var(--text-muted);">Loading conferences...</p>
            </div>
        `;

        try {
            const resp = await api.get('/conferences');
            const list = resp.data || [];
            const tableEl = document.getElementById('admin-conferences-table');

            if (list.length === 0) {
                tableEl.innerHTML = `<p style="padding: 2rem; text-align: center; color: var(--text-muted);">No conferences found in database.</p>`;
                return;
            }

            tableEl.innerHTML = `
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Category</th>
                            <th>Dates</th>
                            <th>Capacity</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${list.map(c => `
                            <tr>
                                <td>#${c.id}</td>
                                <td><strong>${c.title}</strong><br><small style="color: var(--text-muted);">📍 ${c.venue}</small></td>
                                <td><span class="badge badge-primary">${c.category}</span></td>
                                <td>${c.startDate} to ${c.endDate}</td>
                                <td><strong>${c.registeredCount} / ${c.maxParticipants}</strong></td>
                                <td><span class="badge ${c.status === 'UPCOMING' ? 'badge-success' : 'badge-secondary'}">${c.status}</span></td>
                                <td>
                                    <div style="display: flex; gap: 0.35rem;">
                                        <button class="btn btn-secondary btn-sm" onclick="window.adminPages.showEditConferenceModal(${c.id})">Edit</button>
                                        <button class="btn btn-danger btn-sm" onclick="window.adminPages.deleteConference(${c.id})">Delete</button>
                                    </div>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    showCreateConferenceModal: () => {
        window.modal.show({
            title: 'Create New Conference',
            size: 'modal-lg',
            content: `
                <form id="create-conf-form" onsubmit="window.adminPages.handleCreateConference(event)">
                    <div class="form-group">
                        <label>Conference Title *</label>
                        <input type="text" name="title" class="form-control" required placeholder="e.g. IEEE World AI Congress">
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <textarea name="description" class="form-control" placeholder="Detailed conference agenda, topics, and scope..."></textarea>
                    </div>
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Category *</label>
                            <input type="text" name="category" class="form-control" required placeholder="e.g. Artificial Intelligence">
                        </div>
                        <div class="form-group">
                            <label>Venue *</label>
                            <input type="text" name="venue" class="form-control" required placeholder="e.g. Science Auditorium, Hall A">
                        </div>
                    </div>
                    <div class="grid-3">
                        <div class="form-group">
                            <label>Start Date *</label>
                            <input type="date" name="startDate" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>End Date *</label>
                            <input type="date" name="endDate" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Registration Deadline *</label>
                            <input type="date" name="registrationDeadline" class="form-control" required>
                        </div>
                    </div>
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Maximum Capacity *</label>
                            <input type="number" name="maxParticipants" class="form-control" min="1" value="200" required>
                        </div>
                        <div class="form-group">
                            <label>Status</label>
                            <select name="status" class="form-control">
                                <option value="UPCOMING">UPCOMING</option>
                                <option value="ONGOING">ONGOING</option>
                                <option value="COMPLETED">COMPLETED</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                        <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                        <button type="submit" class="btn btn-primary">Save to MySQL</button>
                    </div>
                </form>
            `
        });
    },

    handleCreateConference: async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const payload = {
            title: formData.get('title'),
            description: formData.get('description'),
            category: formData.get('category'),
            venue: formData.get('venue'),
            startDate: formData.get('startDate'),
            endDate: formData.get('endDate'),
            registrationDeadline: formData.get('registrationDeadline'),
            maxParticipants: parseInt(formData.get('maxParticipants')),
            status: formData.get('status')
        };

        try {
            await api.post('/conferences', payload);
            window.modal.close();
            window.toast.show('Conference created and saved to MySQL!', 'success');
            await adminPages.switchTab('conferences');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    showEditConferenceModal: async (id) => {
        try {
            const resp = await api.get(`/conferences/${id}`);
            const c = resp.data;

            window.modal.show({
                title: 'Edit Conference: ' + c.title,
                size: 'modal-lg',
                content: `
                    <form id="edit-conf-form" onsubmit="window.adminPages.handleUpdateConference(event, ${id})">
                        <div class="form-group">
                            <label>Title *</label>
                            <input type="text" name="title" class="form-control" required value="${c.title}">
                        </div>
                        <div class="form-group">
                            <label>Description</label>
                            <textarea name="description" class="form-control">${c.description || ''}</textarea>
                        </div>
                        <div class="grid-2">
                            <div class="form-group">
                                <label>Category *</label>
                                <input type="text" name="category" class="form-control" required value="${c.category}">
                            </div>
                            <div class="form-group">
                                <label>Venue *</label>
                                <input type="text" name="venue" class="form-control" required value="${c.venue}">
                            </div>
                        </div>
                        <div class="grid-3">
                            <div class="form-group">
                                <label>Start Date *</label>
                                <input type="date" name="startDate" class="form-control" required value="${c.startDate}">
                            </div>
                            <div class="form-group">
                                <label>End Date *</label>
                                <input type="date" name="endDate" class="form-control" required value="${c.endDate}">
                            </div>
                            <div class="form-group">
                                <label>Registration Deadline *</label>
                                <input type="date" name="registrationDeadline" class="form-control" required value="${c.registrationDeadline}">
                            </div>
                        </div>
                        <div class="grid-2">
                            <div class="form-group">
                                <label>Maximum Capacity *</label>
                                <input type="number" name="maxParticipants" class="form-control" min="1" required value="${c.maxParticipants}">
                            </div>
                            <div class="form-group">
                                <label>Status</label>
                                <select name="status" class="form-control">
                                    <option value="UPCOMING" ${c.status === 'UPCOMING' ? 'selected' : ''}>UPCOMING</option>
                                    <option value="ONGOING" ${c.status === 'ONGOING' ? 'selected' : ''}>ONGOING</option>
                                    <option value="COMPLETED" ${c.status === 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                                    <option value="CANCELLED" ${c.status === 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                            <button type="submit" class="btn btn-primary">Update in MySQL</button>
                        </div>
                    </form>
                `
            });
        } catch (e) {
            window.toast.show('Failed to fetch conference: ' + e.message, 'error');
        }
    },

    handleUpdateConference: async (e, id) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const payload = {
            title: formData.get('title'),
            description: formData.get('description'),
            category: formData.get('category'),
            venue: formData.get('venue'),
            startDate: formData.get('startDate'),
            endDate: formData.get('endDate'),
            registrationDeadline: formData.get('registrationDeadline'),
            maxParticipants: parseInt(formData.get('maxParticipants')),
            status: formData.get('status')
        };

        try {
            await api.put(`/conferences/${id}`, payload);
            window.modal.close();
            window.toast.show('Conference updated successfully in MySQL!', 'success');
            await adminPages.switchTab('conferences');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    deleteConference: async (id) => {
        if (!confirm('Are you sure you want to permanently delete this conference from MySQL? All associated registrations and sessions will also be removed.')) {
            return;
        }

        try {
            await api.delete(`/conferences/${id}`);
            window.toast.show('Conference deleted successfully from MySQL', 'success');
            await adminPages.switchTab('conferences');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    // --------------------------------------------------------------------------
    // Tab 3: Manage Sessions
    // --------------------------------------------------------------------------
    renderSessionsTab: async (container) => {
        container.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700;">Conference Sessions</h3>
                <button class="btn btn-primary btn-sm" onclick="window.adminPages.showCreateSessionModal()">
                    + Add Session
                </button>
            </div>
            <div id="admin-sessions-table" class="table-responsive">
                <p style="padding: 2rem; text-align: center; color: var(--text-muted);">Loading sessions...</p>
            </div>
        `;

        try {
            const resp = await api.get('/sessions');
            const list = resp.data || [];
            const tableEl = document.getElementById('admin-sessions-table');

            if (list.length === 0) {
                tableEl.innerHTML = `<p style="padding: 2rem; text-align: center; color: var(--text-muted);">No sessions scheduled yet.</p>`;
                return;
            }

            tableEl.innerHTML = `
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Session Title</th>
                            <th>Conference</th>
                            <th>Speaker</th>
                            <th>Date & Time</th>
                            <th>Hall</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${list.map(s => `
                            <tr>
                                <td>#${s.id}</td>
                                <td><strong>${s.title}</strong></td>
                                <td>${s.conferenceTitle}</td>
                                <td>${s.speaker ? s.speaker.user.name : '<span style="color:var(--text-muted)">Unassigned</span>'}</td>
                                <td>${s.sessionDate} (${s.startTime} - ${s.endTime})</td>
                                <td><span class="badge badge-secondary">${s.hall}</span></td>
                                <td>
                                    <button class="btn btn-danger btn-sm" onclick="window.adminPages.deleteSession(${s.id})">Delete</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    showCreateSessionModal: async () => {
        try {
            const [confResp, spkResp] = await Promise.all([
                api.get('/conferences'),
                api.get('/speakers')
            ]);
            const confs = confResp.data || [];
            const speakers = spkResp.data || [];

            if (confs.length === 0) {
                window.toast.show('Please create a conference first before adding sessions.', 'warning');
                return;
            }

            window.modal.show({
                title: 'Create Conference Session',
                content: `
                    <form id="create-session-form" onsubmit="window.adminPages.handleCreateSession(event)">
                        <div class="form-group">
                            <label>Conference *</label>
                            <select name="conferenceId" class="form-control" required>
                                ${confs.map(c => `<option value="${c.id}">${c.title}</option>`).join('')}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Speaker</label>
                            <select name="speakerId" class="form-control">
                                <option value="">-- Unassigned --</option>
                                ${speakers.map(s => `<option value="${s.id}">${s.user.name} (${s.organization})</option>`).join('')}
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Session Title *</label>
                            <input type="text" name="title" class="form-control" required placeholder="e.g. Keynote on Distributed Cloud">
                        </div>
                        <div class="form-group">
                            <label>Description</label>
                            <textarea name="description" class="form-control" placeholder="Session details and takeaways..."></textarea>
                        </div>
                        <div class="grid-3">
                            <div class="form-group">
                                <label>Date *</label>
                                <input type="date" name="sessionDate" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Start Time *</label>
                                <input type="time" name="startTime" class="form-control" required value="09:00">
                            </div>
                            <div class="form-group">
                                <label>End Time *</label>
                                <input type="time" name="endTime" class="form-control" required value="10:30">
                            </div>
                        </div>
                        <div class="grid-2">
                            <div class="form-group">
                                <label>Hall / Room *</label>
                                <input type="text" name="hall" class="form-control" required placeholder="e.g. Auditorium A">
                            </div>
                            <div class="form-group">
                                <label>Max Capacity</label>
                                <input type="number" name="maxCapacity" class="form-control" value="100">
                            </div>
                        </div>
                        <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                            <button type="submit" class="btn btn-primary">Save Session</button>
                        </div>
                    </form>
                `
            });
        } catch (e) {
            window.toast.show('Failed to prepare session form: ' + e.message, 'error');
        }
    },

    handleCreateSession: async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const spkVal = formData.get('speakerId');

        const payload = {
            conferenceId: parseInt(formData.get('conferenceId')),
            speakerId: spkVal ? parseInt(spkVal) : null,
            title: formData.get('title'),
            description: formData.get('description'),
            sessionDate: formData.get('sessionDate'),
            startTime: formData.get('startTime') + ':00',
            endTime: formData.get('endTime') + ':00',
            hall: formData.get('hall'),
            maxCapacity: parseInt(formData.get('maxCapacity') || '100')
        };

        try {
            await api.post('/sessions', payload);
            window.modal.close();
            window.toast.show('Session created successfully!', 'success');
            await adminPages.switchTab('sessions');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    deleteSession: async (id) => {
        if (!confirm('Are you sure you want to delete this session?')) return;
        try {
            await api.delete(`/sessions/${id}`);
            window.toast.show('Session deleted successfully', 'success');
            await adminPages.switchTab('sessions');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    // --------------------------------------------------------------------------
    // Tab 4: Manage Speakers
    // --------------------------------------------------------------------------
    renderSpeakersTab: async (container) => {
        container.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700;">Conference Speakers</h3>
                <button class="btn btn-primary btn-sm" onclick="window.adminPages.showCreateSpeakerModal()">
                    + Add Speaker Profile
                </button>
            </div>
            <div id="admin-speakers-table" class="table-responsive">
                <p style="padding: 2rem; text-align: center; color: var(--text-muted);">Loading speakers...</p>
            </div>
        `;

        try {
            const resp = await api.get('/speakers');
            const list = resp.data || [];
            const tableEl = document.getElementById('admin-speakers-table');

            if (list.length === 0) {
                tableEl.innerHTML = `<p style="padding: 2rem; text-align: center; color: var(--text-muted);">No speakers registered yet.</p>`;
                return;
            }

            tableEl.innerHTML = `
                <table class="table">
                    <thead>
                        <tr>
                            <th>Speaker Name</th>
                            <th>Organization</th>
                            <th>Designation</th>
                            <th>Expertise</th>
                            <th>Phone</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${list.map(s => `
                            <tr>
                                <td><strong>${s.user.name}</strong><br><small style="color:var(--text-muted);">${s.user.email}</small></td>
                                <td>${s.organization}</td>
                                <td>${s.designation}</td>
                                <td><span class="badge badge-primary">${s.expertise}</span></td>
                                <td>${s.phone || '-'}</td>
                                <td>
                                    <button class="btn btn-danger btn-sm" onclick="window.adminPages.deleteSpeaker(${s.id})">Delete</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    showCreateSpeakerModal: async () => {
        try {
            const usersResp = await api.get('/users');
            const users = usersResp.data || [];

            window.modal.show({
                title: 'Add Speaker Profile',
                content: `
                    <form id="create-speaker-form" onsubmit="window.adminPages.handleCreateSpeaker(event)">
                        <div class="form-group">
                            <label>Associate User *</label>
                            <select name="userId" class="form-control" required>
                                ${users.map(u => `<option value="${u.id}">${u.name} (${u.email}) [${u.role}]</option>`).join('')}
                            </select>
                        </div>
                        <div class="grid-2">
                            <div class="form-group">
                                <label>Organization *</label>
                                <input type="text" name="organization" class="form-control" required placeholder="e.g. Stanford University">
                            </div>
                            <div class="form-group">
                                <label>Designation *</label>
                                <input type="text" name="designation" class="form-control" required placeholder="e.g. Professor of Computing">
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Expertise *</label>
                            <input type="text" name="expertise" class="form-control" required placeholder="e.g. AI, Distributed Systems">
                        </div>
                        <div class="form-group">
                            <label>Phone Number</label>
                            <input type="text" name="phone" class="form-control" placeholder="+1 555-0199">
                        </div>
                        <div class="form-group">
                            <label>Biography</label>
                            <textarea name="biography" class="form-control" placeholder="Short professional summary..."></textarea>
                        </div>
                        <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                            <button type="submit" class="btn btn-primary">Save Speaker</button>
                        </div>
                    </form>
                `
            });
        } catch (e) {
            window.toast.show('Failed to prepare speaker modal: ' + e.message, 'error');
        }
    },

    handleCreateSpeaker: async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const payload = {
            userId: parseInt(formData.get('userId')),
            organization: formData.get('organization'),
            designation: formData.get('designation'),
            expertise: formData.get('expertise'),
            phone: formData.get('phone'),
            biography: formData.get('biography')
        };

        try {
            await api.post('/speakers', payload);
            window.modal.close();
            window.toast.show('Speaker profile created successfully!', 'success');
            await adminPages.switchTab('speakers');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    deleteSpeaker: async (id) => {
        if (!confirm('Are you sure you want to delete this speaker profile?')) return;
        try {
            await api.delete(`/speakers/${id}`);
            window.toast.show('Speaker profile deleted', 'success');
            await adminPages.switchTab('speakers');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    // --------------------------------------------------------------------------
    // Tab 5: Manage Research Papers
    // --------------------------------------------------------------------------
    renderPapersTab: async (container) => {
        container.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700;">Submitted Research Papers</h3>
            </div>
            <div id="admin-papers-table" class="table-responsive">
                <p style="padding: 2rem; text-align: center; color: var(--text-muted);">Loading research papers...</p>
            </div>
        `;

        try {
            const resp = await api.get('/papers');
            const list = resp.data || [];
            const tableEl = document.getElementById('admin-papers-table');

            if (list.length === 0) {
                tableEl.innerHTML = `<p style="padding: 2rem; text-align: center; color: var(--text-muted);">No research papers submitted yet.</p>`;
                return;
            }

            tableEl.innerHTML = `
                <table class="table">
                    <thead>
                        <tr>
                            <th>Paper Title</th>
                            <th>Author</th>
                            <th>Conference</th>
                            <th>Status</th>
                            <th>Score</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${list.map(p => {
                            let badgeClass = 'badge-secondary';
                            if (p.status === 'ACCEPTED') badgeClass = 'badge-success';
                            if (p.status === 'REJECTED') badgeClass = 'badge-danger';
                            if (p.status === 'UNDER_REVIEW') badgeClass = 'badge-warning';

                            return `
                                <tr>
                                    <td>
                                        <strong>${p.title}</strong><br>
                                        <small style="color:var(--text-muted);">Category: ${p.category} | Keywords: ${p.keywords}</small>
                                    </td>
                                    <td>${p.author.name}</td>
                                    <td>${p.conferenceTitle}</td>
                                    <td><span class="badge ${badgeClass}">${p.status}</span></td>
                                    <td>
                                        ${p.averageScore ? `<strong>${p.averageScore} / 10</strong> (${p.reviewCount} reviews)` : '<span style="color:var(--text-muted);">Pending</span>'}
                                    </td>
                                    <td>
                                        <div style="display: flex; gap: 0.35rem; flex-wrap: wrap;">
                                            <button class="btn btn-secondary btn-sm" onclick="window.adminPages.showAssignReviewerModal(${p.id})">Assign Reviewer</button>
                                            <button class="btn btn-primary btn-sm" onclick="window.adminPages.showChangePaperStatusModal(${p.id}, '${p.status}')">Set Status</button>
                                            ${p.filePath ? `<a href="http://localhost:8080/api/papers/${p.id}/download" target="_blank" class="btn btn-outline btn-sm">PDF</a>` : ''}
                                        </div>
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

    showAssignReviewerModal: async (paperId) => {
        try {
            const reviewersResp = await api.get('/users?role=REVIEWER');
            const reviewers = reviewersResp.data || [];

            if (reviewers.length === 0) {
                window.toast.show('No users with REVIEWER role found. Please change a user role to REVIEWER first.', 'warning');
                return;
            }

            window.modal.show({
                title: 'Assign Paper to Reviewer',
                content: `
                    <form onsubmit="window.adminPages.handleAssignReviewer(event, ${paperId})">
                        <div class="form-group">
                            <label>Select Peer Reviewer *</label>
                            <select name="reviewerId" class="form-control" required>
                                ${reviewers.map(r => `<option value="${r.id}">${r.name} (${r.email})</option>`).join('')}
                            </select>
                        </div>
                        <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                            <button type="submit" class="btn btn-primary">Confirm Assignment</button>
                        </div>
                    </form>
                `
            });
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    handleAssignReviewer: async (e, paperId) => {
        e.preventDefault();
        const reviewerId = parseInt(new FormData(e.target).get('reviewerId'));

        try {
            await api.post('/reviews/assign', { paperId, reviewerId });
            window.modal.close();
            window.toast.show('Reviewer assigned! Paper status updated to UNDER_REVIEW in MySQL.', 'success');
            await adminPages.switchTab('papers');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    showChangePaperStatusModal: (paperId, currentStatus) => {
        window.modal.show({
            title: 'Update Paper Status',
            content: `
                <form onsubmit="window.adminPages.handleChangePaperStatus(event, ${paperId})">
                    <div class="form-group">
                        <label>Select Decision</label>
                        <select name="status" class="form-control">
                            <option value="ACCEPTED" ${currentStatus === 'ACCEPTED' ? 'selected' : ''}>ACCEPTED</option>
                            <option value="REJECTED" ${currentStatus === 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                            <option value="REVISION_REQUIRED" ${currentStatus === 'REVISION_REQUIRED' ? 'selected' : ''}>REVISION_REQUIRED</option>
                            <option value="UNDER_REVIEW" ${currentStatus === 'UNDER_REVIEW' ? 'selected' : ''}>UNDER_REVIEW</option>
                        </select>
                    </div>
                    <div class="modal-footer" style="padding-left:0; padding-right:0; margin-bottom:-1.5rem;">
                        <button type="button" class="btn btn-secondary" onclick="window.modal.close()">Cancel</button>
                        <button type="submit" class="btn btn-primary">Save Decision</button>
                    </div>
                </form>
            `
        });
    },

    handleChangePaperStatus: async (e, paperId) => {
        e.preventDefault();
        const status = new FormData(e.target).get('status');

        try {
            await api.put(`/papers/${paperId}/status`, { status });
            window.modal.close();
            window.toast.show(`Paper status updated to ${status}! Notification sent to author.`, 'success');
            await adminPages.switchTab('papers');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    },

    // --------------------------------------------------------------------------
    // Tab 6: Manage Users & Roles
    // --------------------------------------------------------------------------
    renderUsersTab: async (container) => {
        container.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700;">System Users & RBAC Roles</h3>
            </div>
            <div id="admin-users-table" class="table-responsive">
                <p style="padding: 2rem; text-align: center; color: var(--text-muted);">Loading users...</p>
            </div>
        `;

        try {
            const resp = await api.get('/users');
            const list = resp.data || [];
            const tableEl = document.getElementById('admin-users-table');

            tableEl.innerHTML = `
                <table class="table">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Current Role</th>
                            <th>Change Role</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${list.map(u => `
                            <tr>
                                <td>#${u.id}</td>
                                <td><strong>${u.name}</strong></td>
                                <td>${u.email}</td>
                                <td><span class="badge badge-primary">${u.role}</span></td>
                                <td>
                                    <select class="form-control" style="width: auto; padding: 0.25rem 0.5rem; font-size: 0.85rem;"
                                            onchange="window.adminPages.changeUserRole(${u.id}, this.value)">
                                        <option value="ADMIN" ${u.role === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                                        <option value="PARTICIPANT" ${u.role === 'PARTICIPANT' ? 'selected' : ''}>PARTICIPANT</option>
                                        <option value="SPEAKER" ${u.role === 'SPEAKER' ? 'selected' : ''}>SPEAKER</option>
                                        <option value="AUTHOR" ${u.role === 'AUTHOR' ? 'selected' : ''}>AUTHOR</option>
                                        <option value="REVIEWER" ${u.role === 'REVIEWER' ? 'selected' : ''}>REVIEWER</option>
                                    </select>
                                </td>
                                <td>
                                    <button class="btn btn-danger btn-sm" onclick="window.adminPages.deleteUser(${u.id})">Delete</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    changeUserRole: async (userId, newRole) => {
        try {
            await api.put(`/users/${userId}/role?role=${newRole}`);
            window.toast.show(`User role updated to ${newRole} in MySQL!`, 'success');
            await adminPages.switchTab('users');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    deleteUser: async (userId) => {
        if (!confirm('Are you sure you want to delete this user from MySQL?')) return;
        try {
            await api.delete(`/users/${userId}`);
            window.toast.show('User deleted from database', 'success');
            await adminPages.switchTab('users');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    // --------------------------------------------------------------------------
    // Tab 7: Manage Certificates
    // --------------------------------------------------------------------------
    renderCertificatesTab: async (container) => {
        container.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700;">Issued Participation Certificates</h3>
            </div>
            <div id="admin-certs-table" class="table-responsive">
                <p style="padding: 2rem; text-align: center; color: var(--text-muted);">Loading certificates...</p>
            </div>
        `;

        try {
            const resp = await api.get('/certificates');
            const list = resp.data || [];
            const tableEl = document.getElementById('admin-certs-table');

            if (list.length === 0) {
                tableEl.innerHTML = `<p style="padding: 2rem; text-align: center; color: var(--text-muted);">No certificates issued yet.</p>`;
                return;
            }

            tableEl.innerHTML = `
                <table class="table">
                    <thead>
                        <tr>
                            <th>Certificate ID</th>
                            <th>Participant</th>
                            <th>Conference</th>
                            <th>Verification Code</th>
                            <th>Issued At</th>
                            <th>Download</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${list.map(c => `
                            <tr>
                                <td><code>${c.certificateNumber}</code></td>
                                <td><strong>${c.participantName}</strong></td>
                                <td>${c.conferenceTitle}</td>
                                <td><span class="badge badge-success">${c.verificationCode}</span></td>
                                <td>${c.generatedAt.replace('T', ' ').substring(0, 16)}</td>
                                <td>
                                    <a href="http://localhost:8080/api/certificates/${c.id}/download" target="_blank" class="btn btn-primary btn-sm">
                                        📥 Download PDF
                                    </a>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        } catch (e) {
            container.innerHTML = `<p style="color: var(--danger); text-align: center; padding: 2rem;">Error: ${e.message}</p>`;
        }
    },

    // --------------------------------------------------------------------------
    // QR Attendance Verification Modal
    // --------------------------------------------------------------------------
    showAttendanceScanner: () => {
        window.modal.show({
            title: '🎟️ QR Code Attendance Verification',
            content: `
                <div>
                    <p style="font-size: 0.9rem; color: var(--text-secondary); margin-bottom: 1.25rem; line-height: 1.5;">
                        Scan the attendee's digital badge QR code using a scanner or paste their registration token / registration ID below.
                        Verifying attendance will update the record in MySQL and immediately unlock their PDF Participation Certificate.
                    </p>
                    <form onsubmit="window.adminPages.handleCheckIn(event)">
                        <div class="form-group">
                            <label>QR Code Data or Registration ID *</label>
                            <input type="text" name="qrCode" class="form-control" required autofocus placeholder="e.g. CS-REG-1-2-DEMO1 or 1">
                        </div>
                        <button type="submit" class="btn btn-success" style="width: 100%;">
                            ✅ Verify & Mark Attendance
                        </button>
                    </form>
                    <div id="checkin-result" style="margin-top: 1.25rem;"></div>
                </div>
            `
        });
    },

    handleCheckIn: async (e) => {
        e.preventDefault();
        const qrCode = new FormData(e.target).get('qrCode').trim();
        const resultEl = document.getElementById('checkin-result');
        if (!resultEl) return;

        resultEl.innerHTML = `<div style="text-align: center; color: var(--text-muted);">Verifying with Spring Boot backend...</div>`;

        try {
            const resp = await api.post('/attendance/check-in', { qrCode });
            const reg = resp.data;

            resultEl.innerHTML = `
                <div style="background-color: var(--success-bg); border: 1px solid var(--success); border-radius: var(--radius-md); padding: 1.25rem; text-align: center;">
                    <h4 style="color: var(--success); font-weight: 800; font-size: 1.15rem; margin-bottom: 0.5rem;">
                        🎉 Check-In Successful!
                    </h4>
                    <p style="font-weight: 700; font-size: 1rem; color: var(--text-primary); margin-bottom: 0.25rem;">
                        ${reg.participant.name} (${reg.participant.email})
                    </p>
                    <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 0.75rem;">
                        Conference: <strong>${reg.conference.title}</strong>
                    </p>
                    <span class="badge badge-success">ATTENDANCE VERIFIED & RECORDED IN MYSQL</span>
                </div>
            `;
            window.toast.show('Attendance verified successfully!', 'success');
        } catch (err) {
            resultEl.innerHTML = `
                <div style="background-color: var(--danger-bg); border: 1px solid var(--danger); border-radius: var(--radius-md); padding: 1rem; text-align: center; color: var(--danger); font-weight: 600;">
                    ❌ Verification Failed: ${err.message}
                </div>
            `;
            window.toast.show(err.message, 'error');
        }
    }
};

window.adminPages = adminPages;

