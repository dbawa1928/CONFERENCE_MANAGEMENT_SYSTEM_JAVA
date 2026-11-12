/**
 * ConfSync Authentication Manager
 * Handles login, registration, role checks, and demo accounts.
 */

import { api } from './api.js';

class AuthManager {
    constructor() {
        this.listeners = [];
    }

    onAuthChange(callback) {
        this.listeners.push(callback);
    }

    notifyAuthChange() {
        const user = this.getCurrentUser();
        this.listeners.forEach(cb => cb(user));
    }

    isAuthenticated() {
        return !!api.getToken() && !!api.getUser();
    }

    getCurrentUser() {
        return api.getUser();
    }

    getRole() {
        const user = this.getCurrentUser();
        return user ? user.role : null;
    }

    async login(email, password) {
        const response = await api.post('/auth/login', { email, password });
        if (response.success && response.data) {
            api.setToken(response.data.token);
            api.setUser(response.data.user);
            this.notifyAuthChange();
            return response.data.user;
        }
        throw new Error(response.message || 'Login failed');
    }

    async register(name, email, password, role) {
        const response = await api.post('/auth/register', { name, email, password, role });
        if (response.success && response.data) {
            // Automatically log in after register
            return await this.login(email, password);
        }
        throw new Error(response.message || 'Registration failed');
    }

    logout() {
        api.clearAuth();
        this.notifyAuthChange();
    }

    // Quick Login presets for PBLJ project evaluations & demonstrations
    async demoLogin(role) {
        const demoCredentials = {
            ADMIN: { email: 'admin@confsync.com', password: 'admin123' },
            PARTICIPANT: { email: 'john.doe@university.edu', password: 'password123' },
            SPEAKER: { email: 'alan.turing@cambridge.edu', password: 'password123' },
            AUTHOR: { email: 'alex.chen@stanford.edu', password: 'password123' },
            REVIEWER: { email: 'david.miller@berkeley.edu', password: 'password123' }
        };

        const creds = demoCredentials[role];
        if (creds) {
            return await this.login(creds.email, creds.password);
        }
        throw new Error('Invalid demo role: ' + role);
    }
}

export const auth = new AuthManager();

