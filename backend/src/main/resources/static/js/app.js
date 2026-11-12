/**
 * ConfSync Frontend Application Controller
 * Client-Side Router, UI State, Modals, Toasts & Navbar
 */

import { api } from './api.js';
import { auth } from './auth.js';
import { publicPages } from './pages/public.js';
import { adminPages } from './pages/admin.js';
import { participantPages } from './pages/participant.js';
import { authorPages } from './pages/author.js';
import { reviewerPages } from './pages/reviewer.js';
import { speakerPages } from './pages/speaker.js';

// ==============================================================================
// 1. Toast Notification Service
// ==============================================================================
class ToastService {
    constructor() {
        this.container = document.getElementById('toast-container');
    }

    show(message, type = 'info') {
        if (!this.container) {
            this.container = document.getElementById('toast-container');
        }

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;

        let icon = 'ℹ️';
        if (type === 'success') icon = '✅';
        if (type === 'error') icon = '❌';
        if (type === 'warning') icon = '⚠️';

        toast.innerHTML = `
            <div style="font-size: 1.25rem;">${icon}</div>
            <div style="flex: 1; font-size: 0.9rem; font-weight: 500;">${message}</div>
        `;

        this.container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(50px)';
            toast.style.transition = 'all 0.3s ease';
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    }
}

// ==============================================================================
// 2. Modal Service
// ==============================================================================
class ModalService {
    constructor() {
        this.container = document.getElementById('modal-container');
    }

    show({ title, content, footer, size = '' }) {
        if (!this.container) {
            this.container = document.getElementById('modal-container');
        }

        this.container.innerHTML = `
            <div class="modal-backdrop" onclick="if(event.target === this) window.modal.close()">
                <div class="modal ${size}">
                    <div class="modal-header">
                        <h3 style="font-size: 1.2rem; font-weight: 700;">${title}</h3>
                        <button class="modal-close" onclick="window.modal.close()">&times;</button>
                    </div>
                    <div class="modal-body">
                        ${content}
                    </div>
                    ${footer ? `<div class="modal-footer">${footer}</div>` : ''}
                </div>
            </div>
        `;
    }

    close() {
        if (this.container) {
            this.container.innerHTML = '';
        }
    }

    showLogin() {
        this.show({
            title: 'Sign In to ConfSync',
            content: `
                <form id="login-form" onsubmit="window.app.handleLogin(event)">
                    <div class="form-group">
                        <label>Email Address</label>
                        <input type="email" name="email" class="form-control" required placeholder="name@university.edu" autofocus>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" class="form-control" required placeholder="••••••••">
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%; margin-bottom: 1rem;">
                        Sign In
                    </button>
                    <div style="text-align: center; font-size: 0.875rem;">
                        Don't have an account? 
                        <a href="#" style="color: var(--primary); font-weight: 600;" onclick="event.preventDefault(); window.modal.showRegister()">
                            Create an Account
                        </a>
                    </div>
                </form>
                <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--border-color); text-align: center;">
                    <button class="btn btn-secondary btn-sm" style="width: 100%;" onclick="window.modal.showDemoLogin()">
                        🚀 Fast-Login with Demo Accounts
                    </button>
                </div>
            `
        });
    }

    showRegister() {
        this.show({
            title: 'Create ConfSync Account',
            content: `
                <form id="register-form" onsubmit="window.app.handleRegister(event)">
                    <div class="form-group">
                        <label>Full Name *</label>
                        <input type="text" name="name" class="form-control" required placeholder="Dr. Jane Smith">
                    </div>
                    <div class="form-group">
                        <label>Email Address *</label>
                        <input type="email" name="email" class="form-control" required placeholder="jane.smith@mit.edu">
                    </div>
                    <div class="form-group">
                        <label>Password *</label>
                        <input type="password" name="password" class="form-control" minlength="6" required placeholder="At least 6 characters">
                    </div>
                    <div class="form-group">
                        <label>Primary Role *</label>
                        <select name="role" class="form-control" required>
                            <option value="PARTICIPANT">PARTICIPANT (Attend conferences, earn certificates)</option>
                            <option value="AUTHOR">AUTHOR (Submit research papers for peer-review)</option>
                            <option value="REVIEWER">REVIEWER (Evaluate submitted papers)</option>
                            <option value="SPEAKER">SPEAKER (Keynotes & technical workshops)</option>
                            <option value="ADMIN">ADMIN (Full conference & system management)</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary" style="width: 100%; margin-bottom: 1rem;">
                        Register Account
                    </button>
                    <div style="text-align: center; font-size: 0.875rem;">
                        Already registered? 
                        <a href="#" style="color: var(--primary); font-weight: 600;" onclick="event.preventDefault(); window.modal.showLogin()">
                            Sign In
                        </a>
                    </div>
                </form>
            `
        });
    }

    showDemoLogin() {
        this.show({
            title: '🚀 University PBLJ Demo Fast-Login',
            content: `
                <p style="font-size: 0.875rem; color: var(--text-secondary); margin-bottom: 1.25rem;">
                    Select any role below to instantly authenticate and evaluate the system without typing credentials:
                </p>
                <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                    <button class="btn btn-primary" style="justify-content: space-between;" onclick="window.app.demoLogin('ADMIN')">
                        <span>👑 <strong>Administrator / Organizer</strong></span>
                        <small style="opacity: 0.8;">admin@confsync.com</small>
                    </button>
                    <button class="btn btn-secondary" style="justify-content: space-between;" onclick="window.app.demoLogin('PARTICIPANT')">
                        <span>🎟️ <strong>Conference Participant</strong></span>
                        <small style="color: var(--text-muted);">john.doe@university.edu</small>
                    </button>
                    <button class="btn btn-secondary" style="justify-content: space-between;" onclick="window.app.demoLogin('AUTHOR')">
                        <span>📄 <strong>Paper Author / Researcher</strong></span>
                        <small style="color: var(--text-muted);">alex.chen@stanford.edu</small>
                    </button>
                    <button class="btn btn-secondary" style="justify-content: space-between;" onclick="window.app.demoLogin('REVIEWER')">
                        <span>🔍 <strong>Peer Reviewer</strong></span>
                        <small style="color: var(--text-muted);">david.miller@berkeley.edu</small>
                    </button>
                    <button class="btn btn-secondary" style="justify-content: space-between;" onclick="window.app.demoLogin('SPEAKER')">
                        <span>🎤 <strong>Keynote Speaker</strong></span>
                        <small style="color: var(--text-muted);">alan.turing@cambridge.edu</small>
                    </button>
                </div>
            `
        });
    }
}

// ==============================================================================
// 3. Client-Side Router
// ==============================================================================
class Router {
    constructor() {
        this.contentEl = document.getElementById('app-content');
        window.addEventListener('popstate', () => this.handleRoute());
    }

    navigate(path) {
        window.history.pushState({}, '', path);
        this.handleRoute();
    }

    async handleRoute() {
        const path = window.location.pathname;
        window.app.updateNavbar();

        // Public Routes
        if (path === '/' || path === '/home') {
            await publicPages.renderHome(this.contentEl);
            return;
        }
        if (path === '/conferences') {
            await publicPages.renderConferences(this.contentEl);
            return;
        }
        if (path === '/about') {
            publicPages.renderAbout(this.contentEl);
            return;
        }
        if (path === '/contact') {
            publicPages.renderContact(this.contentEl);
            return;
        }

        // Authenticated Dashboard Routes
        if (path === '/dashboard') {
            if (!auth.isAuthenticated()) {
                window.toast.show('Please log in to access the dashboard', 'warning');
                window.modal.showLogin();
                this.navigate('/');
                return;
            }

            const role = auth.getRole();
            if (role === 'ADMIN') {
                await adminPages.renderDashboard(this.contentEl);
            } else if (role === 'PARTICIPANT') {
                await participantPages.renderDashboard(this.contentEl);
            } else if (role === 'AUTHOR') {
                await authorPages.renderDashboard(this.contentEl);
            } else if (role === 'REVIEWER') {
                await reviewerPages.renderDashboard(this.contentEl);
            } else if (role === 'SPEAKER') {
                await speakerPages.renderDashboard(this.contentEl);
            } else {
                await participantPages.renderDashboard(this.contentEl);
            }
            return;
        }

        if (path === '/my-registrations' || path === '/my-schedule' || path === '/my-certificates') {
            if (!auth.isAuthenticated()) {
                window.modal.showLogin();
                return;
            }
            await participantPages.renderDashboard(this.contentEl);
            return;
        }

        if (path === '/my-papers') {
            if (!auth.isAuthenticated()) {
                window.modal.showLogin();
                return;
            }
            await authorPages.renderDashboard(this.contentEl);
            return;
        }

        // 404 Route
        this.contentEl.innerHTML = `
            <div class="card" style="text-align: center; padding: 4rem 1.5rem;">
                <h1 style="font-size: 3rem; font-weight: 800; color: var(--primary);">404</h1>
                <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 0.5rem;">Page Not Found</h2>
                <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">The requested page does not exist.</p>
                <button class="btn btn-primary" onclick="window.router.navigate('/')">Go to Home</button>
            </div>
        `;
    }
}

// ==============================================================================
// 4. Main App Controller
// ==============================================================================
class App {
    constructor() {
        window.toast = new ToastService();
        window.modal = new ModalService();
        window.router = new Router();
    }

    init() {
        auth.onAuthChange(() => {
            this.updateNavbar();
            window.router.handleRoute();
        });

        this.updateNavbar();
        window.router.handleRoute();
    }

    updateNavbar() {
        const navActions = document.getElementById('nav-actions');
        if (!navActions) return;

        if (auth.isAuthenticated()) {
            const user = auth.getCurrentUser();
            navActions.innerHTML = `
                <button class="btn btn-primary btn-sm" onclick="window.router.navigate('/dashboard')">
                    📊 Dashboard
                </button>
                <div class="user-menu" onclick="window.app.toggleUserMenu(event)">
                    <div class="user-avatar">${user.name.charAt(0).toUpperCase()}</div>
                    <span>${user.name}</span>
                    <span class="badge badge-primary" style="font-size: 0.7rem;">${user.role}</span>
                </div>
                <button class="btn btn-secondary btn-sm" onclick="window.app.logout()">
                    Sign Out
                </button>
            `;
        } else {
            navActions.innerHTML = `
                <button class="btn btn-secondary btn-sm" onclick="window.modal.showDemoLogin()">
                    🚀 Demo Login
                </button>
                <button class="btn btn-primary btn-sm" onclick="window.modal.showLogin()">
                    Sign In
                </button>
            `;
        }
    }

    async handleLogin(e) {
        e.preventDefault();
        const form = e.target;
        const email = form.email.value;
        const password = form.password.value;

        try {
            await auth.login(email, password);
            window.modal.close();
            window.toast.show('Signed in successfully!', 'success');
            window.router.navigate('/dashboard');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    }

    async handleRegister(e) {
        e.preventDefault();
        const form = e.target;
        const name = form.name.value;
        const email = form.email.value;
        const password = form.password.value;
        const role = form.role.value;

        try {
            await auth.register(name, email, password, role);
            window.modal.close();
            window.toast.show('Account created successfully!', 'success');
            window.router.navigate('/dashboard');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    }

    async demoLogin(role) {
        try {
            await auth.demoLogin(role);
            window.modal.close();
            window.toast.show(`Logged in as ${role}!`, 'success');
            window.router.navigate('/dashboard');
        } catch (err) {
            window.toast.show(err.message, 'error');
        }
    }

    logout() {
        auth.logout();
        window.toast.show('Signed out', 'info');
        window.router.navigate('/');
    }
}

window.app = new App();
document.addEventListener('DOMContentLoaded', () => {
    window.app.init();
});

