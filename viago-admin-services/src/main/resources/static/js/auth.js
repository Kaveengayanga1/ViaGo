/**
 * ViaGO Admin - Authentication Module
 * Handles login, logout, and JWT token management
 */

const Auth = (function () {
    // Configuration - Auth service URL
    const AUTH_SERVICE_URL = 'http://localhost:8080';
    const ADMIN_SERVICE_URL = 'http://localhost:8081';

    // Storage keys
    const TOKEN_KEY = 'viago_admin_token';
    const REFRESH_TOKEN_KEY = 'viago_admin_refresh_token';
    const USER_DATA_KEY = 'viago_admin_user';
    const TOKEN_EXPIRY_KEY = 'viago_admin_token_expiry';

    /**
     * Login with email and password
     * @param {string} email 
     * @param {string} password 
     * @returns {Promise<{success: boolean, message: string}>}
     */
    async function login(email, password) {
        try {
            const response = await fetch(`${AUTH_SERVICE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    identifier: email,
                    password: password
                })
            });

            const data = await response.json();

            if (data.success && data.jwtToken) {
                // Store tokens and user data
                localStorage.setItem(TOKEN_KEY, data.jwtToken);

                if (data.refreshToken) {
                    localStorage.setItem(REFRESH_TOKEN_KEY, data.refreshToken);
                }

                if (data.data) {
                    localStorage.setItem(USER_DATA_KEY, JSON.stringify(data.data));
                }

                if (data.expiresIn) {
                    const expiryTime = Date.now() + (data.expiresIn * 1000);
                    localStorage.setItem(TOKEN_EXPIRY_KEY, expiryTime.toString());
                }

                return { success: true, message: 'Login successful' };
            } else {
                return {
                    success: false,
                    message: data.message || 'Invalid credentials'
                };
            }
        } catch (error) {
            console.error('Login error:', error);
            return {
                success: false,
                message: 'Unable to connect to authentication service'
            };
        }
    }

    /**
     * Logout - Clear all stored tokens and user data
     */
    function logout() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(REFRESH_TOKEN_KEY);
        localStorage.removeItem(USER_DATA_KEY);
        localStorage.removeItem(TOKEN_EXPIRY_KEY);
    }

    /**
     * Check if user is authenticated
     * @returns {boolean}
     */
    function isAuthenticated() {
        const token = getToken();
        if (!token) return false;

        // Check if token is expired
        const expiry = localStorage.getItem(TOKEN_EXPIRY_KEY);
        if (expiry && Date.now() > parseInt(expiry)) {
            logout();
            return false;
        }

        return true;
    }

    /**
     * Get stored JWT token
     * @returns {string|null}
     */
    function getToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    /**
     * Get stored user data
     * @returns {object|null}
     */
    function getUserData() {
        const data = localStorage.getItem(USER_DATA_KEY);
        return data ? JSON.parse(data) : null;
    }

    /**
     * Get authorization header for API calls
     * @returns {object}
     */
    function getAuthHeader() {
        const token = getToken();
        return token ? { 'Authorization': `Bearer ${token}` } : {};
    }

    /**
     * Make authenticated API call to admin service
     * @param {string} endpoint 
     * @param {object} options 
     * @returns {Promise<Response>}
     */
    async function apiCall(endpoint, options = {}) {
        if (!isAuthenticated()) {
            window.location.href = 'login.html';
            throw new Error('Not authenticated');
        }

        const headers = {
            'Content-Type': 'application/json',
            ...getAuthHeader(),
            ...options.headers
        };

        const response = await fetch(`${ADMIN_SERVICE_URL}${endpoint}`, {
            ...options,
            headers
        });

        // Handle 401 Unauthorized
        if (response.status === 401) {
            logout();
            window.location.href = 'login.html';
            throw new Error('Session expired');
        }

        return response;
    }

    /**
     * Require authentication - redirect to login if not authenticated
     */
    function requireAuth() {
        if (!isAuthenticated()) {
            window.location.href = 'login.html';
            return false;
        }
        return true;
    }

    // Public API
    return {
        login,
        logout,
        isAuthenticated,
        getToken,
        getUserData,
        getAuthHeader,
        apiCall,
        requireAuth,
        ADMIN_SERVICE_URL
    };
})();
