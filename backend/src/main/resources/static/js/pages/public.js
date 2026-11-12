/**
 * Public Pages: Home, Conferences, Conference Details, About, Contact, Auth Modals
 */

import { api } from '../api.js';
import { auth } from '../auth.js';

export const publicPages = {
    renderHome: async (container) => {
        container.innerHTML = `
            <div class="hero">
                <div class="hero-content">
                    <div class="hero-pill">☕ PBLJ University Java Project</div>
                    <h1 class="hero-title">ConfSync — Smart Conference Management System</h1>
                    <p class="hero-description">
                        An enterprise-grade Java Spring Boot, Hibernate & MySQL architecture demonstrating Core Java,
                        Object-Oriented Programming, Collections, File Handling, and QR Attendance.
                    </p>
                    <div class="hero-actions">
                        <button class="btn btn-primary btn-lg" onclick="window.router.navigate('/conferences')">
                            📅 Browse Conferences
                        </button>
                        ${!auth.isAuthenticated() ? `
                            <button class="btn btn-secondary btn-lg" onclick="window.modal.showDemoLogin()">
                                🚀 Quick Demo Login
                            </button>
                        ` : `
                            <button class="btn btn-secondary btn-lg" onclick="window.router.navigate('/dashboard')">
                                📊 Go to Dashboard
                            </button>
                        `}
                    </div>
                </div>
            </div>

            <!-- PBLJ Java Architecture Showcase -->
            <div class="page-header" style="margin-top: 1rem;">
                <div>
                    <h2 class="page-title" style="font-size: 1.5rem;">Java Architecture & Technologies</h2>
                    <p class="page-subtitle">Key Java PBLJ concepts implemented in this application</p>
                </div>
            </div>

            <div class="grid-4" style="margin-bottom: 3rem;">
                <div class="card">
                    <div class="stat-icon blue" style="margin-bottom: 0.75rem;">☕</div>
                    <h3 style="font-size: 1.05rem; font-weight: 700; margin-bottom: 0.35rem;">Core Java & OOP</h3>
                    <p style="font-size: 0.85rem; color: var(--text-secondary);">
                        Encapsulation, Polymorphism (Service & Impl), Abstraction (BaseEntity), and Enum types (Role, Status).
                    </p>
                </div>
                <div class="card">
                    <div class="stat-icon green" style="margin-bottom: 0.75rem;">🗃️</div>
                    <h3 style="font-size: 1.05rem; font-weight: 700; margin-bottom: 0.35rem;">MySQL & JPA</h3>
                    <p style="font-size: 0.85rem; color: var(--text-secondary);">
                        Spring Data JPA with Hibernate, Relational mapping (1:N, N:1, 1:1), and persistent storage on localhost:3306.
                    </p>
                </div>
                <div class="card">
                    <div class="stat-icon orange" style="margin-bottom: 0.75rem;">🛡️</div>
                    <h3 style="font-size: 1.05rem; font-weight: 700; margin-bottom: 0.35rem;">Security & Exceptions</h3>
                    <p style="font-size: 0.85rem; color: var(--text-secondary);">
                        Spring Security, BCrypt hashing, JWT tokens, and centralized @RestControllerAdvice exception handling.
                    </p>
                </div>
                <div class="card">
                    <div class="stat-icon purple" style="margin-bottom: 0.75rem;">📄</div>
                    <h3 style="font-size: 1.05rem; font-weight: 700; margin-bottom: 0.35rem;">File I/O & QR Codes</h3>
                    <p style="font-size: 0.85rem; color: var(--text-secondary);">
                        Local PDF research paper uploads, ZXing QR generation, and Apache PDFBox participation certificate generation.
                    </p>
                </div>
            </div>

            <!-- Featured Upcoming Conferences Section -->
            <div class="page-header">
                <div>
                    <h2 class="page-title" style="font-size: 1.5rem;">Upcoming Conferences</h2>
                    <p class="page-subtitle">Explore academic and technical summits stored in MySQL</p>
                </div>
                <button class="btn btn-secondary btn-sm" onclick="window.router.navigate('/conferences')">
                    View All →
                </button>
            </div>

            <div id="home-conferences-container" class="grid-3">
                <div style="grid-column: 1 / -1; text-align: center; padding: 2rem; color: var(--text-muted);">
                    Loading upcoming conferences from MySQL...
                </div>
            </div>
        `;

        try {
            const resp = await api.get('/conferences/upcoming');
            const confs = resp.data || [];
            const containerEl = document.getElementById('home-conferences-container');
            if (confs.length === 0) {
                containerEl.innerHTML = `<div style="grid-column: 1 / -1; text-align: center; padding: 2rem; color: var(--text-muted);">No upcoming conferences found in database.</div>`;
                return;
            }

            containerEl.innerHTML = confs.slice(0, 3).map(conf => publicPages.renderConferenceCard(conf)).join('');
        } catch (e) {
            console.error('Failed to load upcoming conferences:', e);
            const containerEl = document.getElementById('home-conferences-container');
            if (containerEl) {
                containerEl.innerHTML = `<div style="grid-column: 1 / -1; text-align: center; padding: 2rem; color: var(--danger);">Could not connect to Spring Boot backend at http://localhost:8080. Please ensure the backend is running.</div>`;
            }
        }
    },

    renderConferenceCard: (conf) => {
        const capacityPercent = conf.maxParticipants > 0 ? 
            Math.min(100, Math.round((conf.registeredCount / conf.maxParticipants) * 100)) : 0;

        return `
            <div class="card" style="display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.75rem;">
                        <span class="badge badge-primary">${conf.category}</span>
                        <span class="badge ${conf.status === 'UPCOMING' ? 'badge-success' : 'badge-secondary'}">${conf.status}</span>
                    </div>
                    <h3 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 0.5rem; line-height: 1.3;">
                        ${conf.title}
                    </h3>
                    <p style="font-size: 0.875rem; color: var(--text-secondary); margin-bottom: 1rem; line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden;">
                        ${conf.description || 'Join academic researchers and industry pioneers at this conference.'}
                    </p>
                    <div style="font-size: 0.825rem; color: var(--text-muted); margin-bottom: 0.5rem;">
                        📍 <strong>Venue:</strong> ${conf.venue}
                    </div>
                    <div style="font-size: 0.825rem; color: var(--text-muted); margin-bottom: 1rem;">
                        📅 <strong>Dates:</strong> ${conf.startDate} to ${conf.endDate}
                    </div>
                </div>

                <div>
                    <div style="margin-bottom: 1rem;">
                        <div style="display: flex; justify-content: space-between; font-size: 0.8rem; margin-bottom: 0.25rem;">
                            <span>Capacity: <strong>${conf.registeredCount} / ${conf.maxParticipants}</strong></span>
                            <span>${conf.remainingCapacity} seats left</span>
                        </div>
                        <div style="width: 100%; height: 6px; background-color: var(--border-color); border-radius: var(--radius-full); overflow: hidden;">
                            <div style="width: ${capacityPercent}%; height: 100%; background-color: ${capacityPercent > 85 ? 'var(--danger)' : 'var(--primary)'};"></div>
                        </div>
                    </div>
                    <div style="display: flex; gap: 0.5rem;">
                        <button class="btn btn-secondary btn-sm" style="flex: 1;" onclick="window.publicPages.showConferenceDetails(${conf.id})">
                            Details
                        </button>
                        <button class="btn btn-primary btn-sm" style="flex: 1;" onclick="window.publicPages.registerForConference(${conf.id})">
                            Register
                        </button>
                    </div>
                </div>
            </div>
        `;
    },

    renderConferences: async (container) => {
        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">Conferences Directory</h1>
                    <p class="page-subtitle">Search, filter, and register for academic and technical conferences</p>
                </div>
            </div>

            <div class="card" style="margin-bottom: 1.5rem; padding: 1.25rem;">
                <div style="display: grid; grid-template-columns: 2fr 1fr 1fr auto; gap: 1rem; align-items: end;">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label>Search Keyword</label>
                        <input type="text" id="conf-search-input" class="form-control" placeholder="Search title, venue, description...">
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label>Category</label>
                        <select id="conf-category-select" class="form-control">
                            <option value="">All Categories</option>
                            <option value="Artificial Intelligence">Artificial Intelligence</option>
                            <option value="Cloud Computing">Cloud Computing</option>
                            <option value="Cybersecurity">Cybersecurity</option>
                            <option value="Software Engineering">Software Engineering</option>
                            <option value="Data Science">Data Science</option>
                        </select>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label>Status</label>
                        <select id="conf-status-select" class="form-control">
                            <option value="">All Statuses</option>
                            <option value="UPCOMING">UPCOMING</option>
                            <option value="ONGOING">ONGOING</option>
                            <option value="COMPLETED">COMPLETED</option>
                        </select>
                    </div>
                    <button class="btn btn-primary" onclick="window.publicPages.filterConferences()">
                        🔍 Filter
                    </button>
                </div>
            </div>

            <div id="conferences-grid" class="grid-3">
                <div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: var(--text-muted);">
                    Loading conferences from MySQL...
                </div>
            </div>
        `;

        await publicPages.filterConferences();
    },

    filterConferences: async () => {
        const search = document.getElementById('conf-search-input')?.value.trim() || '';
        const category = document.getElementById('conf-category-select')?.value || '';
        const status = document.getElementById('conf-status-select')?.value || '';

        const grid = document.getElementById('conferences-grid');
        if (!grid) return;

        grid.innerHTML = `<div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: var(--text-muted);">Searching conferences...</div>`;

        try {
            const params = new URLSearchParams();
            if (search) params.append('search', search);
            if (category) params.append('category', category);
            if (status) params.append('status', status);

            const queryString = params.toString() ? `?${params.toString()}` : '';
            const resp = await api.get(`/conferences${queryString}`);
            const confs = resp.data || [];

            if (confs.length === 0) {
                grid.innerHTML = `<div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: var(--text-muted);">No conferences found matching your search criteria.</div>`;
                return;
            }

            grid.innerHTML = confs.map(c => publicPages.renderConferenceCard(c)).join('');
        } catch (e) {
            grid.innerHTML = `<div style="grid-column: 1 / -1; text-align: center; padding: 3rem; color: var(--danger);">Failed to load conferences: ${e.message}</div>`;
        }
    },

    showConferenceDetails: async (confId) => {
        try {
            const [confResp, sessionsResp] = await Promise.all([
                api.get(`/conferences/${confId}`),
                api.get(`/sessions/conference/${confId}`)
            ]);

            const conf = confResp.data;
            const sessions = sessionsResp.data || [];

            window.modal.show({
                title: conf.title,
                size: 'modal-lg',
                content: `
                    <div>
                        <div style="display: flex; gap: 0.5rem; margin-bottom: 1rem;">
                            <span class="badge badge-primary">${conf.category}</span>
                            <span class="badge badge-success">${conf.status}</span>
                        </div>
                        <p style="font-size: 0.95rem; color: var(--text-secondary); margin-bottom: 1.5rem; line-height: 1.6;">
                            ${conf.description}
                        </p>

                        <div class="grid-2" style="margin-bottom: 1.5rem; background-color: #f8fafc; padding: 1rem; border-radius: var(--radius-md);">
                            <div>
                                <p style="font-size: 0.85rem; color: var(--text-muted);">Venue</p>
                                <p style="font-weight: 600;">📍 ${conf.venue}</p>
                            </div>
                            <div>
                                <p style="font-size: 0.85rem; color: var(--text-muted);">Dates</p>
                                <p style="font-weight: 600;">📅 ${conf.startDate} to ${conf.endDate}</p>
                            </div>
                            <div>
                                <p style="font-size: 0.85rem; color: var(--text-muted);">Registration Deadline</p>
                                <p style="font-weight: 600;">⏰ ${conf.registrationDeadline}</p>
                            </div>
                            <div>
                                <p style="font-size: 0.85rem; color: var(--text-muted);">Capacity</p>
                                <p style="font-weight: 600;">👥 ${conf.registeredCount} / ${conf.maxParticipants} (${conf.remainingCapacity} available)</p>
                            </div>
                        </div>

                        <h3 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 1rem;">🗓️ Sessions Agenda</h3>
                        ${sessions.length === 0 ? `
                            <p style="font-size: 0.9rem; color: var(--text-muted);">No sessions scheduled yet for this conference.</p>
                        ` : `
                            <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                                ${sessions.map(s => `
                                    <div style="border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 0.85rem; background: white;">
                                        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.25rem;">
                                            <strong style="color: var(--primary); font-size: 0.95rem;">${s.title}</strong>
                                            <span class="badge badge-secondary">${s.hall}</span>
                                        </div>
                                        <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 0.35rem;">${s.description || ''}</p>
                                        <div style="font-size: 0.8rem; color: var(--text-muted); display: flex; gap: 1rem;">
                                            <span>🕒 ${s.sessionDate} (${s.startTime} - ${s.endTime})</span>
                                            ${s.speaker ? `<span>🎤 Speaker: <strong>${s.speaker.user.name}</strong> (${s.speaker.organization})</span>` : ''}
                                        </div>
                                    </div>
                                `).join('')}
                            </div>
                        `}
                    </div>
                `,
                footer: `
                    <button class="btn btn-secondary" onclick="window.modal.close()">Close</button>
                    <button class="btn btn-primary" onclick="window.modal.close(); window.publicPages.registerForConference(${conf.id})">
                        Register for Conference
                    </button>
                `
            });
        } catch (e) {
            window.toast.show('Failed to fetch conference details: ' + e.message, 'error');
        }
    },

    registerForConference: async (confId) => {
        if (!auth.isAuthenticated()) {
            window.toast.show('Please log in or register an account to sign up for this conference', 'warning');
            window.modal.showLogin();
            return;
        }

        try {
            const resp = await api.post('/registrations', { conferenceId: confId });
            window.toast.show(resp.message || 'Successfully registered for conference!', 'success');
            // Navigate to My Registrations to view digital pass & QR code
            window.router.navigate('/my-registrations');
        } catch (e) {
            window.toast.show(e.message, 'error');
        }
    },

    renderAbout: (container) => {
        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">About ConfSync & PBLJ Java Project</h1>
                    <p class="page-subtitle">Project Based Learning in Java (PBLJ) Comprehensive Architecture</p>
                </div>
            </div>

            <div class="card" style="margin-bottom: 2rem;">
                <h2 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 1rem; color: var(--primary);">
                    🎓 Project Overview
                </h2>
                <p style="font-size: 0.95rem; color: var(--text-secondary); line-height: 1.7; margin-bottom: 1.25rem;">
                    <strong>ConfSync</strong> is an enterprise-grade Smart Conference Management System built specifically for
                    university Java course evaluation (PBLJ). It provides a full-stack, distributed-ready architecture where
                    real business logic, persistence, security, file storage, and data validation are centralized in a robust
                    Java Spring Boot backend running on <code>http://localhost:8080</code> and connected to MySQL on <code>localhost:3306</code>.
                </p>

                <h3 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 0.75rem;">
                    ☕ Key Java Concepts Demonstrated
                </h3>
                <ul style="padding-left: 1.5rem; line-height: 1.8; color: var(--text-secondary); margin-bottom: 1.5rem;">
                    <li><strong>Object-Oriented Programming:</strong> Strict Encapsulation (private fields, getters, setters), Polymorphism (Service interfaces with Spring @Service implementations), Inheritance & Abstraction (BaseEntity mapped superclass with audit timestamps).</li>
                    <li><strong>Java Collections Framework:</strong> Extensive use of <code>List</code>, <code>Set</code>, and <code>Map</code> along with Java 8+ Stream API for category aggregation, role classification, and duplicate elimination.</li>
                    <li><strong>Exception Handling:</strong> Centralized <code>@RestControllerAdvice</code> providing unified HTTP 200, 201, 400, 401, 403, 404, 409, 500 status codes with custom business exceptions (<code>DuplicateRegistrationException</code>, <code>ConferenceCapacityException</code>, <code>ResourceNotFoundException</code>).</li>
                    <li><strong>File Handling (Java NIO):</strong> Local file uploads and storage for PDF research papers in <code>backend/uploads/papers/</code> and programmatic PDF participation certificate generation using Apache PDFBox.</li>
                    <li><strong>Database Connectivity:</strong> Spring Data JPA and Hibernate ORM interacting with MySQL <code>confsync_db</code> database schema.</li>
                    <li><strong>Security & Cryptography:</strong> Spring Security with BCrypt password encryption, stateless JWT authentication token provider, and role-based authorization filters.</li>
                </ul>

                <div style="background-color: var(--primary-light); padding: 1.25rem; border-radius: var(--radius-md); border-left: 4px solid var(--primary);">
                    <strong>💡 Submission Details:</strong> All data is persistent. Refreshing the browser or restarting the backend will preserve all users, conferences, sessions, papers, and attendance records directly in MySQL.
                </div>
            </div>
        `;
    },

    renderContact: (container) => {
        container.innerHTML = `
            <div class="page-header">
                <div>
                    <h1 class="page-title">Contact & Academic Support</h1>
                    <p class="page-subtitle">Get in touch with the ConfSync project committee</p>
                </div>
            </div>

            <div class="grid-2">
                <div class="card">
                    <h3 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 1rem;">Send a Message</h3>
                    <form onsubmit="event.preventDefault(); window.toast.show('Thank you! Your message has been received.', 'success'); this.reset();">
                        <div class="form-group">
                            <label>Your Name</label>
                            <input type="text" class="form-control" required placeholder="Enter full name">
                        </div>
                        <div class="form-group">
                            <label>Your Email</label>
                            <input type="email" class="form-control" required placeholder="Enter email address">
                        </div>
                        <div class="form-group">
                            <label>Subject</label>
                            <input type="text" class="form-control" required placeholder="Subject inquiry">
                        </div>
                        <div class="form-group">
                            <label>Message</label>
                            <textarea class="form-control" rows="4" required placeholder="Write your inquiry here..."></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%;">
                            ✉️ Send Message
                        </button>
                    </form>
                </div>

                <div class="card" style="display: flex; flex-direction: column; justify-content: space-between;">
                    <div>
                        <h3 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 1rem;">Academic System Info</h3>
                        <p style="font-size: 0.9rem; color: var(--text-secondary); margin-bottom: 1.5rem; line-height: 1.6;">
                            This application is configured for local university deployment.
                        </p>
                        <div style="font-size: 0.875rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: 0.75rem;">
                            <div>📍 <strong>Host:</strong> Localhost (Spring Boot & MySQL)</div>
                            <div>☕ <strong>Backend:</strong> http://localhost:8080</div>
                            <div>💻 <strong>Frontend:</strong> http://localhost:5173</div>
                            <div>🗄️ <strong>Database:</strong> MySQL 3306 (confsync_db)</div>
                        </div>
                    </div>

                    <div style="margin-top: 2rem; background: #f8fafc; padding: 1.25rem; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                        <strong style="display: block; margin-bottom: 0.5rem;">Need to test quickly?</strong>
                        <button class="btn btn-secondary btn-sm" style="width: 100%;" onclick="window.modal.showDemoLogin()">
                            🚀 Click here to Fast-Login as any Role
                        </button>
                    </div>
                </div>
            </div>
        `;
    }
};

window.publicPages = publicPages;

