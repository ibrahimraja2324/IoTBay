<%@ page import="java.util.List" %>
<%@ page import="iotbay.model.User" %>
<%@ page import="iotbay.dao.UserDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="iotbay.dao.DBManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>View Users - IoTBay</title>
  <link rel="stylesheet" href="style.css">
  <style>
    .search-container {
      margin: 20px 0;
      display: flex;
      gap: 10px;
      align-items: center;
    }
    .search-container input {
      padding: 8px;
      border: 1px solid #ddd;
      border-radius: 4px;
      width: 200px;
    }
    .search-container select {
      padding: 8px;
      border: 1px solid #ddd;
      border-radius: 4px;
      width: 200px;
      min-width: 0;
    }
    .pagination {
      margin-top: 20px;
      display: flex;
      justify-content: center;
      gap: 10px;
    }
    .pagination a {
      padding: 8px 12px;
      border: 1px solid #ddd;
      border-radius: 4px;
      text-decoration: none;
      color: #333;
      background-color: white;
    }
    .pagination a:hover {
      background-color: #f8f9fa;
    }
    .pagination a.active {
      background-color: #007bff;
      color: white;
      border-color: #007bff;
    }
    .sort-header {
      cursor: pointer;
      user-select: none;
    }
    .sort-header:hover {
      background-color: #f8f9fa;
    }
    .user-details td.actions-cell {
      white-space: normal;
    }
    .user-details td.actions-cell a,
    .user-details td.actions-cell button {
      display: block;
      margin-bottom: 6px;
      width: 100%;
      box-sizing: border-box;
    }
    .user-details td.actions-cell a:last-child,
    .user-details td.actions-cell button:last-child {
      margin-bottom: 0;
    }
    .dashboard-section {
      overflow-x: auto;
      
    }
  </style>
</head>
<body>
  <nav class="page-nav">
    <div class="nav-left">
      <a href="main.jsp">Home</a>
    </div>
    <div class="nav-right">
      <a href="LogoutServlet">Logout</a>
    </div>
  </nav>

  <div class="dashboard-container">
    <h1 class="dashboard-title">User Management</h1>

    <div class="dashboard-section">
      <a href="createUser.jsp" class="btn-add-shipment">Create New User</a>
    </div>

    <div class="dashboard-section">
      <h2>All Users</h2>
      
      <div class="search-container">
        <input type="text" id="searchInput" placeholder="Search users by email, phone, or name..." onkeyup="filterTable()">
        <select id="roleFilter" onchange="filterTable()">
          <option value="">All Roles</option>
          <option value="ADMIN">Admin</option>
          <option value="STAFF">Staff</option>
          <option value="USER">User</option>
        </select>
        <select id="statusFilter" onchange="filterTable()">
          <option value="">All Status</option>
          <option value="active">Active</option>
          <option value="inactive">Inactive</option>
        </select>
      </div>

      <table class="user-details wide" id="userTable">
        <tr>
          <th class="sort-header" onclick="sortTable(0)">Email ↕</th>
          <th class="sort-header" onclick="sortTable(1)">Name ↕</th>
          <th class="sort-header" onclick="sortTable(2)">Phone ↕</th>
          <th class="sort-header" onclick="sortTable(3)">Role ↕</th>
          <th class="sort-header" onclick="sortTable(4)">Status ↕</th>
          <th>Actions</th>
        </tr>

        <%
          DBManager manager = (DBManager) session.getAttribute("manager");
          User currentUser = (User) session.getAttribute("currentUser");
          boolean isAdmin = currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
          boolean isStaff = currentUser != null && "STAFF".equalsIgnoreCase(currentUser.getRole());
          
          if (manager != null) {
              UserDAO userDAO = new UserDAO(manager.getConnection());
              List<User> users = userDAO.getAllUsers();
              // Add debug logging
              System.out.println("Total users loaded: " + users.size());
              for (User u : users) {
                  boolean isAdminAccount = "admin@example.com".equalsIgnoreCase(u.getEmail());
                  boolean isCurrentUser = currentUser != null && currentUser.getEmail().equals(u.getEmail());
                  boolean isStaffAccount = "STAFF".equalsIgnoreCase(u.getRole());
                  // Staff can edit regular users, other staff accounts, and their own account
                  boolean canModify = isAdmin || (isStaff && !isAdminAccount) || isCurrentUser;
                  // Staff can only delete/deactivate regular users and their own account
                  boolean canDeleteOrDeactivate = isAdmin || (isStaff && (!isStaffAccount || isCurrentUser)) || isCurrentUser;
        %>
        <tr>
          <td><%= u.getEmail() %></td>
          <td><%= u.getFirstName() + " " + u.getLastName() %></td>
          <td><%= u.getPhone() %></td>
          <td><%= u.getRole() %></td>
          <td>
            <% if (u.isActive()) { %>
              <span class="status-badge status-processing">Active</span>
            <% } else { %>
              <span class="status-badge status-delivered">Inactive</span>
            <% } %>
          </td>
          <td class="actions-cell">
            <% if (canModify) { %>
              <a href="UserServlet?action=edit&email=<%= u.getEmail() %>" 
                 class="btn-secondary" style="padding: 6px 10px; font-size: 13px;">Edit</a>
            
              <% if (!isAdminAccount && canDeleteOrDeactivate) { %>
                <a href="UserServlet?action=delete&email=<%= u.getEmail() %>" 
                   class="btn-important" style="padding: 6px 10px; font-size: 13px;"
                   onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>

                <% String action = u.isActive() ? "deactivate" : "activate"; %>
                <a href="UserServlet?action=toggleStatus&email=<%= u.getEmail() %>" 
                   class="btn-secondary" style="padding: 6px 10px; font-size: 13px;"
                   onclick="return confirm('Are you sure you want to <%= action %> this user?');">
                   <%= u.isActive() ? "Deactivate" : "Activate" %>
                </a>
              <% } %>
            <% } else { %>
              <span class="text-muted">No actions available</span>
            <% } %>
          </td>
        </tr>
        <%
              }
          } else {
        %>
        <tr>
          <td colspan="6">Error: DBManager not initialized. Please log in again.</td>
        </tr>
        <%
          }
        %>
      </table>

      <div class="pagination" id="pagination"></div>
    </div>
  </div>

  <script>
    let currentPage = 1;
    const rowsPerPage = 10;
    let sortColumn = 0;
    let sortDirection = 1;

    function getFilteredRows() {
      const table = document.getElementById('userTable');
      const rows = Array.from(table.getElementsByTagName('tr')).slice(1);
      const searchText = document.getElementById('searchInput').value.toLowerCase();
      const roleFilter = document.getElementById('roleFilter').value;
      const statusFilter = document.getElementById('statusFilter').value;

      return rows.filter(row => {
        const cells = row.getElementsByTagName('td');
        let showRow = true;

        // Search text filter
        if (searchText) {
          let found = false;
          for (let j = 0; j < cells.length - 1; j++) {
            if (cells[j].textContent.toLowerCase().includes(searchText)) {
              found = true;
              break;
            }
          }
          if (!found) showRow = false;
        }

        // Role filter
        if (roleFilter && cells[3].textContent !== roleFilter) {
          showRow = false;
        }

        // Status filter
        if (statusFilter) {
          const status = cells[4].textContent.trim().toLowerCase();
          if (statusFilter === 'active' && status !== 'active') showRow = false;
          if (statusFilter === 'inactive' && status !== 'inactive') showRow = false;
        }

        return showRow;
      });
    }

    function showPage() {
      const rows = Array.from(document.getElementById('userTable').getElementsByTagName('tr')).slice(1);
      const filteredRows = getFilteredRows();
      const start = (currentPage - 1) * rowsPerPage;
      const end = start + rowsPerPage;

      // Hide all rows first
      rows.forEach(row => row.style.display = 'none');

      // Show only the filtered rows for the current page
      filteredRows.forEach((row, index) => {
        if (index >= start && index < end) {
          row.style.display = '';
        }
      });
    }

    function updatePagination() {
      const filteredRows = getFilteredRows();
      const totalPages = Math.ceil(filteredRows.length / rowsPerPage);
      const pagination = document.getElementById('pagination');
      pagination.innerHTML = '';

      // Previous button
      const prevButton = document.createElement('a');
      prevButton.href = '#';
      prevButton.textContent = 'Previous';
      prevButton.onclick = (e) => {
        e.preventDefault();
        if (currentPage > 1) {
          currentPage--;
          showPage();
          updatePagination();
        }
      };
      pagination.appendChild(prevButton);

      // Page numbers
      for (let i = 1; i <= totalPages; i++) {
        const pageLink = document.createElement('a');
        pageLink.href = '#';
        pageLink.textContent = i;
        if (i === currentPage) {
          pageLink.className = 'active';
        }
        pageLink.onclick = (e) => {
          e.preventDefault();
          currentPage = i;
          showPage();
          updatePagination();
        };
        pagination.appendChild(pageLink);
      }

      // Next button
      const nextButton = document.createElement('a');
      nextButton.href = '#';
      nextButton.textContent = 'Next';
      nextButton.onclick = (e) => {
        e.preventDefault();
        if (currentPage < totalPages) {
          currentPage++;
          showPage();
          updatePagination();
        }
      };
      pagination.appendChild(nextButton);

      showPage();
    }

    // Update event handlers to reset to page 1 and update pagination
    document.getElementById('searchInput').onkeyup = function() {
      currentPage = 1;
      updatePagination();
    };
    document.getElementById('roleFilter').onchange = function() {
      currentPage = 1;
      updatePagination();
    };
    document.getElementById('statusFilter').onchange = function() {
      currentPage = 1;
      updatePagination();
    };

    // Initialize
    updatePagination();
  </script>
</body>
</html>
