<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.LeaveType" %>

<div class="container" x-data="leaveForm()">
    <!-- Form điền đơn -->
    <div class="form-box">
        <h1>Tạo đơn xin nghỉ phép</h1>

        <form action="${pageContext.request.contextPath}/request/create" method="post" 
              @submit.prevent="confirmSubmit($event)">
            
            <label for="leaveType">Loại nghỉ phép:</label>
            <select id="leaveType" name="typeid" required>
                <option value="" disabled selected>Chọn loại nghỉ</option>
                <% 
                    ArrayList<LeaveType> leaveTypes = (ArrayList<LeaveType>) request.getAttribute("leaveTypes");
                    if (leaveTypes != null) {
                        for (LeaveType lt : leaveTypes) {
                %>
                <option value="<%= lt.getId() %>"><%= lt.getTypename() %></option>
                <% 
                        }
                    }
                %>
            </select>

            <label for="startDate">Từ ngày:</label>
            <input type="date" id="startDate" name="startDate" required @change="calculateDays()">

            <label for="endDate">Đến ngày:</label>
            <input type="date" id="endDate" name="endDate" required @change="calculateDays()">

            <template x-if="numDays > 0">
                <p><strong>Số ngày nghỉ dự kiến:</strong> <span x-text="numDays"></span> ngày</p>
            </template>

            <label for="reason">Lý do nghỉ:</label>
            <textarea id="reason" name="reason" placeholder="Nhập lý do nghỉ..." required></textarea>

            <button type="submit" class="btn-submit">Gửi Đơn</button>
        </form>

        <c:if test="${not empty error}">
            <div class="error" x-data="{show:true}" x-init="setTimeout(()=>show=false,3000)" x-show="show">
                ${error}
            </div>
        </c:if>
    </div>

    <!-- Quy định -->
    <div class="info-box">
        <h2>Quy định nghỉ phép</h2>
        <ul>
            <li>Mỗi nhân viên được nghỉ phép tối đa 12 ngày/năm.</li>
            <li>Đơn xin nghỉ phải gửi trước ít nhất 2 ngày làm việc.</li>
            <li>Nếu nghỉ bệnh, cần kèm giấy chứng nhận y tế.</li>
            <li>Người quản lý có quyền phê duyệt hoặc từ chối đơn.</li>
            <li>Ngày lễ và cuối tuần không tính vào ngày nghỉ phép.</li>
        </ul>
        <p>Vui lòng điền đầy đủ thông tin để đơn được phê duyệt nhanh chóng.</p>
    </div>
</div>

<script>
function leaveForm() {
    return {
        numDays: 0,
        calculateDays() {
            const start = document.getElementById('startDate').value;
            const end = document.getElementById('endDate').value;
            if (start && end) {
                const diff = (new Date(end) - new Date(start)) / (1000 * 60 * 60 * 24) + 1;
                this.numDays = diff > 0 ? diff : 0;
            }
        },
        confirmSubmit(e) {
            if (confirm('Bạn có chắc muốn gửi đơn xin nghỉ phép không?')) {
                e.target.submit();
            }
        }
    }
}
</script>

<style>
.container {
    display: flex;
    flex: 1;
    flex-direction: row;
    width: 100%;
    height: 100%;
}
.form-box, .info-box {
    flex: 1;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    padding: 2rem;
    margin: 1rem;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
}
h1, h2 {
    margin-top: 0;
    color: #1d7484;
    text-align: center;
}
form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}
label { font-weight: 600; }
input, select, textarea {
    width: 100%;
    padding: 12px 15px;
    font-size: 1rem;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #f9f9f9;
    transition: border 0.2s, box-shadow 0.2s;
}
input:focus, select:focus, textarea:focus {
    border: 1px solid #1d7484;
    box-shadow: 0 0 5px rgba(29,116,132,0.3);
    outline: none;
}
textarea { min-height: 120px; }
.btn-submit {
    background-color: #1d7484;
    color: white;
    border: none;
    padding: 14px;
    border-radius: 8px;
    font-size: 1.1rem;
    cursor: pointer;
    transition: background-color 0.2s;
}
.btn-submit:hover { background-color: #982c61; }
.error {
    color: white;
    background-color: #ff4d4d;
    padding: 10px;
    border-radius: 6px;
    text-align: center;
    font-weight: 600;
    margin-top: 1rem;
    animation: fadeIn 0.4s ease;
}
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(-10px); }
    to { opacity: 1; transform: translateY(0); }
}
.info-box {
    background-color: #eef7f9;
    line-height: 1.6;
}
.info-box ul { padding-left: 1.4em; }
@media (max-width: 1024px) {
    .container { flex-direction: column; }
}
</style>