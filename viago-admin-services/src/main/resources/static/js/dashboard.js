/**
 * ViaGO Admin - Dashboard Module
 * Handles user management operations
 */

(function () {
    // State
    let currentPage = 0;
    const pageSize = 10;
    let currentRole = '';
    let isSearchMode = false;
    let allUsers = [];
    let userToDelete = null;
    let userToEdit = null;

    // DOM Elements
    const elements = {
        usersTableBody: document.getElementById('usersTableBody'),
        roleFilter: document.getElementById('roleFilter'),
        searchEmail: document.getElementById('searchEmail'),
        searchBtn: document.getElementById('searchBtn'),
        clearSearchBtn: document.getElementById('clearSearchBtn'),
        prevPageBtn: document.getElementById('prevPageBtn'),
        nextPageBtn: document.getElementById('nextPageBtn'),
        pageInfo: document.getElementById('pageInfo'),
        logoutBtn: document.getElementById('logoutBtn'),
        userName: document.getElementById('userName'),
        userAvatar: document.getElementById('userAvatar'),
        dashboardAlert: document.getElementById('dashboardAlert'),
        loadingOverlay: document.getElementById('loadingOverlay'),
        loadingText: document.getElementById('loadingText'),
        // Stats
        riderCount: document.getElementById('riderCount'),
        driverCount: document.getElementById('driverCount'),
        adminCount: document.getElementById('adminCount'),
        employeeCount: document.getElementById('employeeCount'),
        // Edit Modal
        editModal: document.getElementById('editModal'),
        editUserForm: document.getElementById('editUserForm'),
        editUserId: document.getElementById('editUserId'),
        editUsername: document.getElementById('editUsername'),
        editEmail: document.getElementById('editEmail'),
        editPassword: document.getElementById('editPassword'),
        editRole: document.getElementById('editRole'),
        closeEditModal: document.getElementById('closeEditModal'),
        cancelEditBtn: document.getElementById('cancelEditBtn'),
        saveEditBtn: document.getElementById('saveEditBtn'),
        saveEditText: document.getElementById('saveEditText'),
        saveEditSpinner: document.getElementById('saveEditSpinner'),
        // Delete Modal
        deleteModal: document.getElementById('deleteModal'),
        deleteUsername: document.getElementById('deleteUsername'),
        deleteEmail: document.getElementById('deleteEmail'),
        closeDeleteModal: document.getElementById('closeDeleteModal'),
        cancelDeleteBtn: document.getElementById('cancelDeleteBtn'),
        confirmDeleteBtn: document.getElementById('confirmDeleteBtn'),
        deleteText: document.getElementById('deleteText'),
        deleteSpinner: document.getElementById('deleteSpinner')
    };

    // Initialize
    document.addEventListener('DOMContentLoaded', init);

    function init() {
        // Check authentication
        if (!Auth.requireAuth()) return;

        // Set user info
        const userData = Auth.getUserData();
        if (userData) {
            elements.userName.textContent = userData.username || userData.email || 'Admin';
            elements.userAvatar.textContent = (userData.username || userData.email || 'A')[0].toUpperCase();
        }

        // Load initial data
        loadUsers();
        loadStats();

        // Setup event listeners
        setupEventListeners();
    }

    function setupEventListeners() {
        // Logout
        elements.logoutBtn.addEventListener('click', handleLogout);

        // Role filter
        elements.roleFilter.addEventListener('change', handleRoleFilter);

        // Search
        elements.searchBtn.addEventListener('click', handleSearch);
        elements.searchEmail.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') handleSearch();
        });
        elements.clearSearchBtn.addEventListener('click', clearSearch);

        // Pagination
        elements.prevPageBtn.addEventListener('click', () => changePage(-1));
        elements.nextPageBtn.addEventListener('click', () => changePage(1));

        // Edit Modal
        elements.closeEditModal.addEventListener('click', closeEditModal);
        elements.cancelEditBtn.addEventListener('click', closeEditModal);
        elements.saveEditBtn.addEventListener('click', saveUserEdit);

        // Delete Modal
        elements.closeDeleteModal.addEventListener('click', closeDeleteModal);
        elements.cancelDeleteBtn.addEventListener('click', closeDeleteModal);
        elements.confirmDeleteBtn.addEventListener('click', confirmDelete);

        // Close modals on overlay click
        elements.editModal.addEventListener('click', (e) => {
            if (e.target === elements.editModal) closeEditModal();
        });
        elements.deleteModal.addEventListener('click', (e) => {
            if (e.target === elements.deleteModal) closeDeleteModal();
        });
    }

    // ========== API Calls ==========

    async function loadUsers() {
        try {
            let endpoint = `/admin/users?page=${currentPage}&size=${pageSize}`;
            if (currentRole) {
                endpoint += `&role=${currentRole}`;
            }

            const response = await Auth.apiCall(endpoint);
            const data = await response.json();

            if (data.success && data.data) {
                allUsers = Array.isArray(data.data) ? data.data : [data.data];
                renderUsersTable(allUsers);
                updatePagination(allUsers.length);
            } else {
                showAlert(data.message || 'Failed to load users', 'error');
                renderEmptyState('Failed to load users');
            }
        } catch (error) {
            console.error('Error loading users:', error);
            renderEmptyState('Error connecting to server');
        }
    }

    async function loadStats() {
        try {
            // Load counts for each role
            const roles = ['RIDER', 'DRIVER', 'ADMIN', 'EMPLOYEE'];
            const counts = {};

            for (const role of roles) {
                try {
                    const response = await Auth.apiCall(`/admin/users?role=${role}&page=0&size=1`);
                    const data = await response.json();
                    // The count would ideally come from backend pagination info
                    // For now we'll show a placeholder or try to get total
                    counts[role] = data.success && data.data ?
                        (Array.isArray(data.data) ? data.data.length : 1) : 0;
                } catch {
                    counts[role] = 0;
                }
            }

            elements.riderCount.textContent = counts.RIDER || '0';
            elements.driverCount.textContent = counts.DRIVER || '0';
            elements.adminCount.textContent = counts.ADMIN || '0';
            elements.employeeCount.textContent = counts.EMPLOYEE || '0';
        } catch (error) {
            console.error('Error loading stats:', error);
        }
    }

    async function searchByEmail(email) {
        try {
            showLoading('Searching...');
            const response = await Auth.apiCall(`/admin/users/${encodeURIComponent(email)}`);
            const data = await response.json();
            hideLoading();

            if (data.success && data.data) {
                allUsers = Array.isArray(data.data) ? data.data : [data.data];
                renderUsersTable(allUsers);
                elements.clearSearchBtn.style.display = 'inline-flex';
                isSearchMode = true;
                updatePagination(1);
                showAlert(`Found user: ${email}`, 'success');
            } else {
                showAlert(data.message || 'User not found', 'error');
                renderEmptyState('No user found with that email');
            }
        } catch (error) {
            hideLoading();
            console.error('Search error:', error);
            showAlert('Error searching for user', 'error');
        }
    }

    async function updateUser(userData) {
        try {
            const response = await Auth.apiCall('/admin/users', {
                method: 'PUT',
                body: JSON.stringify(userData)
            });
            const data = await response.json();
            return { success: data.success, message: data.message };
        } catch (error) {
            console.error('Update error:', error);
            return { success: false, message: 'Failed to update user' };
        }
    }

    async function deleteUser(userId) {
        try {
            const response = await Auth.apiCall(`/admin/users/${userId}`, {
                method: 'DELETE'
            });
            const data = await response.json();
            return { success: data.success !== false, message: data.message };
        } catch (error) {
            console.error('Delete error:', error);
            return { success: false, message: 'Failed to delete user' };
        }
    }

    async function suspendUser(userId) {
        try {
            const response = await Auth.apiCall(`/admin/users/${userId}/suspend`, {
                method: 'PATCH'
            });
            const data = await response.json();
            return { success: data.success !== false, message: data.message || 'User suspended' };
        } catch (error) {
            console.error('Suspend error:', error);
            return { success: false, message: 'Failed to suspend user' };
        }
    }

    async function activateUser(userId) {
        try {
            const response = await Auth.apiCall(`/admin/users/${userId}/activate`, {
                method: 'PATCH'
            });
            const data = await response.json();
            return { success: data.success !== false, message: data.message || 'User activated' };
        } catch (error) {
            console.error('Activate error:', error);
            return { success: false, message: 'Failed to activate user' };
        }
    }

    // ========== Event Handlers ==========

    function handleLogout() {
        Auth.logout();
        window.location.href = 'login.html';
    }

    function handleRoleFilter() {
        currentRole = elements.roleFilter.value;
        currentPage = 0;
        isSearchMode = false;
        elements.clearSearchBtn.style.display = 'none';
        elements.searchEmail.value = '';
        loadUsers();
    }

    function handleSearch() {
        const email = elements.searchEmail.value.trim();
        if (!email) {
            showAlert('Please enter an email address', 'error');
            return;
        }
        searchByEmail(email);
    }

    function clearSearch() {
        isSearchMode = false;
        elements.clearSearchBtn.style.display = 'none';
        elements.searchEmail.value = '';
        currentPage = 0;
        loadUsers();
    }

    function changePage(delta) {
        if (isSearchMode) return;

        currentPage += delta;
        if (currentPage < 0) currentPage = 0;
        loadUsers();
    }

    // ========== Edit User ==========

    function openEditModal(user) {
        userToEdit = user;
        elements.editUserId.value = user.userId;
        elements.editUsername.value = user.username || '';
        elements.editEmail.value = user.email || '';
        elements.editPassword.value = '';
        elements.editRole.value = user.role || 'RIDER';
        elements.editModal.classList.add('show');
    }

    function closeEditModal() {
        elements.editModal.classList.remove('show');
        userToEdit = null;
    }

    async function saveUserEdit() {
        if (!userToEdit) return;

        const userData = {
            userId: parseInt(elements.editUserId.value),
            username: elements.editUsername.value.trim(),
            email: elements.editEmail.value.trim(),
            role: elements.editRole.value,
            enabled: userToEdit.enabled
        };

        // Only include password if changed
        const newPassword = elements.editPassword.value;
        if (newPassword) {
            userData.password = newPassword;
        }

        // UI feedback
        elements.saveEditBtn.disabled = true;
        elements.saveEditText.style.display = 'none';
        elements.saveEditSpinner.style.display = 'inline-block';

        const result = await updateUser(userData);

        elements.saveEditBtn.disabled = false;
        elements.saveEditText.style.display = 'inline';
        elements.saveEditSpinner.style.display = 'none';

        if (result.success) {
            showAlert('User updated successfully', 'success');
            closeEditModal();
            loadUsers();
        } else {
            showAlert(result.message || 'Failed to update user', 'error');
        }
    }

    // ========== Delete User ==========

    function openDeleteModal(user) {
        userToDelete = user;
        elements.deleteUsername.textContent = user.username || '-';
        elements.deleteEmail.textContent = user.email || '-';
        elements.deleteModal.classList.add('show');
    }

    function closeDeleteModal() {
        elements.deleteModal.classList.remove('show');
        userToDelete = null;
    }

    async function confirmDelete() {
        if (!userToDelete) return;

        // UI feedback
        elements.confirmDeleteBtn.disabled = true;
        elements.deleteText.style.display = 'none';
        elements.deleteSpinner.style.display = 'inline-block';

        const result = await deleteUser(userToDelete.userId);

        elements.confirmDeleteBtn.disabled = false;
        elements.deleteText.style.display = 'inline';
        elements.deleteSpinner.style.display = 'none';

        if (result.success) {
            showAlert('User deleted successfully', 'success');
            closeDeleteModal();
            loadUsers();
            loadStats();
        } else {
            showAlert(result.message || 'Failed to delete user', 'error');
        }
    }

    // ========== Suspend/Activate ==========

    async function handleSuspend(user) {
        showLoading('Suspending user...');
        const result = await suspendUser(user.userId);
        hideLoading();

        if (result.success) {
            showAlert('User suspended', 'success');
            loadUsers();
        } else {
            showAlert(result.message || 'Failed to suspend user', 'error');
        }
    }

    async function handleActivate(user) {
        showLoading('Activating user...');
        const result = await activateUser(user.userId);
        hideLoading();

        if (result.success) {
            showAlert('User activated', 'success');
            loadUsers();
        } else {
            showAlert(result.message || 'Failed to activate user', 'error');
        }
    }

    // ========== Rendering ==========

    function renderUsersTable(users) {
        if (!users || users.length === 0) {
            renderEmptyState('No users found');
            return;
        }

        const html = users.map(user => {
            const isActive = user.enabled !== false;
            const roleBadgeClass = `badge-${(user.role || 'rider').toLowerCase()}`;
            const statusBadgeClass = isActive ? 'badge-active' : 'badge-suspended';
            const statusText = isActive ? 'Active' : 'Suspended';

            return `
                <tr data-user-id="${user.userId}">
                    <td>${user.userId || '-'}</td>
                    <td>${escapeHtml(user.username || '-')}</td>
                    <td>${escapeHtml(user.email || '-')}</td>
                    <td><span class="badge ${roleBadgeClass}">${user.role || 'N/A'}</span></td>
                    <td><span class="badge ${statusBadgeClass}">${statusText}</span></td>
                    <td>
                        <div class="action-btns">
                            <button class="action-btn action-btn-edit" title="Edit User" data-action="edit" data-user='${JSON.stringify(user).replace(/'/g, "&#39;")}'>
                                ✎
                            </button>
                            ${isActive ? `
                                <button class="action-btn action-btn-suspend" title="Suspend User" data-action="suspend" data-user='${JSON.stringify(user).replace(/'/g, "&#39;")}'>
                                    ⏸
                                </button>
                            ` : `
                                <button class="action-btn action-btn-activate" title="Activate User" data-action="activate" data-user='${JSON.stringify(user).replace(/'/g, "&#39;")}'>
                                    ▶
                                </button>
                            `}
                            <button class="action-btn action-btn-delete" title="Delete User" data-action="delete" data-user='${JSON.stringify(user).replace(/'/g, "&#39;")}'>
                                🗑
                            </button>
                        </div>
                    </td>
                </tr>
            `;
        }).join('');

        elements.usersTableBody.innerHTML = html;

        // Attach event listeners to action buttons
        elements.usersTableBody.querySelectorAll('[data-action]').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const action = e.currentTarget.getAttribute('data-action');
                const user = JSON.parse(e.currentTarget.getAttribute('data-user'));

                switch (action) {
                    case 'edit':
                        openEditModal(user);
                        break;
                    case 'delete':
                        openDeleteModal(user);
                        break;
                    case 'suspend':
                        handleSuspend(user);
                        break;
                    case 'activate':
                        handleActivate(user);
                        break;
                }
            });
        });
    }

    function renderEmptyState(message) {
        elements.usersTableBody.innerHTML = `
            <tr>
                <td colspan="6">
                    <div class="empty-state">
                        <div class="empty-icon">📭</div>
                        <h3 class="empty-title">${escapeHtml(message)}</h3>
                        <p>Try adjusting your search or filter criteria</p>
                    </div>
                </td>
            </tr>
        `;
    }

    function updatePagination(itemCount) {
        elements.pageInfo.textContent = `Page ${currentPage + 1}`;
        elements.prevPageBtn.disabled = currentPage === 0 || isSearchMode;
        elements.nextPageBtn.disabled = itemCount < pageSize || isSearchMode;
    }

    // ========== Utilities ==========

    function showAlert(message, type = 'error') {
        elements.dashboardAlert.textContent = message;
        elements.dashboardAlert.className = `alert alert-${type} show`;
        setTimeout(() => {
            elements.dashboardAlert.classList.remove('show');
        }, 5000);
    }

    function showLoading(text = 'Loading...') {
        elements.loadingText.textContent = text;
        elements.loadingOverlay.classList.add('show');
    }

    function hideLoading() {
        elements.loadingOverlay.classList.remove('show');
    }

    function escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

})();
