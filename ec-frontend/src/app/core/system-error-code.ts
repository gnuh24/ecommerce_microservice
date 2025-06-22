export interface SystemErrorDetail {
    code: string;
    message: string;
}

export const SystemErrorCode = {
    // ==== AUTHENTICATION / TOKEN ====
    AUTH_MISSING_TOKEN: {
        code: 'SYS-AUTH-001',
        message: 'Thiếu token xác thực',
    },
    AUTH_EXPIRED_TOKEN: {
        code: 'SYS-AUTH-002',
        message: 'Access token đã hết hạn',
    },
    AUTH_TOKEN_INVALID_SIGNATURE: {
        code: 'SYS-AUTH-003',
        message: 'Access token sai chữ ký',
    },
    AUTH_TOKEN_INVALID_TYP: {
        code: 'SYS-AUTH-004',
        message: 'Access token chứa typ không hợp lệ',
    },
    AUTH_TOKEN_UNKNOWN_SUBJECT: {
        code: 'SYS-AUTH-005',
        message: 'Access token chứa subject không tồn tại',
    },
    AUTH_TOKEN_MALFORMED: {
        code: 'SYS-AUTH-006',
        message: 'Access token không đúng định dạng',
    },
    AUTH_TOKEN_UNSUPPORTED: {
        code: 'SYS-AUTH-007',
        message: 'Access token sử dụng thuật toán không được hỗ trợ',
    },
    AUTH_TOKEN_BLACKLISTED: {
        code: 'SYS-AUTH-008',
        message: 'Access token đã bị thu hồi hoặc nằm trong blacklist',
    },
    AUTH_TOKEN_UNKNOWN_ERROR: {
        code: 'SYS-AUTH-009',
        message: 'Lỗi không xác định khi xử lý token',
    },

    // ==== REFRESH TOKEN ====
    AUTH_MISSING_REFRESH_TOKEN: {
        code: 'SYS-AUTH-010',
        message: 'Thiếu refresh token',
    },
    AUTH_REFRESH_TOKEN_EXPIRED: {
        code: 'SYS-AUTH-011',
        message: 'Refresh token đã hết hạn',
    },
    AUTH_REFRESH_TOKEN_INVALID_SIGNATURE: {
        code: 'SYS-AUTH-012',
        message: 'Refresh token sai chữ ký',
    },
    AUTH_REFRESH_TOKEN_INVALID_TYP: {
        code: 'SYS-AUTH-013',
        message: 'Refresh token chứa typ không hợp lệ',
    },
    AUTH_REFRESH_TOKEN_UNKNOWN_SUBJECT: {
        code: 'SYS-AUTH-014',
        message: 'Refresh token chứa subject không tồn tại',
    },
    AUTH_REFRESH_TOKEN_MALFORMED: {
        code: 'SYS-AUTH-015',
        message: 'Refresh token không đúng định dạng',
    },
    AUTH_REFRESH_TOKEN_UNSUPPORTED: {
        code: 'SYS-AUTH-016',
        message: 'Refresh token sử dụng thuật toán không được hỗ trợ',
    },
    AUTH_REFRESH_TOKEN_BLACKLISTED: {
        code: 'SYS-AUTH-017',
        message: 'Refresh token đã bị thu hồi hoặc nằm trong blacklist',
    },
    AUTH_REFRESH_TOKEN_UNKNOWN_ERROR: {
        code: 'SYS-AUTH-018',
        message: 'Lỗi không xác định khi xử lý refresh token',
    },

    // ==== AUTHENTICATION / ACCOUNT ====
    AUTH_INVALID_CREDENTIALS: {
        code: 'SYS-AUTH-019',
        message: 'Email hoặc mật khẩu không đúng',
    },
    AUTH_ACCOUNT_NOT_FOUND: {
        code: 'SYS-AUTH-020',
        message: 'Email hoặc mật khẩu không đúng',
    },
    AUTH_ACCOUNT_LOCKED: {
        code: 'SYS-AUTH-021',
        message: 'Tài khoản bị khóa',
    },
    AUTH_ACCOUNT_INACTIVE: {
        code: 'SYS-AUTH-022',
        message: 'Tài khoản chưa được kích hoạt',
    },
    AUTH_ACCOUNT_ALREADY_EXISTS: {
        code: 'SYS-AUTH-023',
        message: 'Tài khoản đã tồn tại',
    },
    AUTH_ACCESS_DENIED: {
        code: 'SYS-AUTH-024',
        message: 'Không có quyền truy cập',
    },

    // ==== OTP ====
    AUTH_OTP_INVALID: {
        code: 'SYS-AUTH-025',
        message: 'Mã OTP không hợp lệ',
    },
    AUTH_OTP_EXPIRED: {
        code: 'SYS-AUTH-026',
        message: 'Mã OTP đã hết hạn',
    },
    AUTH_OTP_TOO_MANY_ATTEMPTS: {
        code: 'SYS-AUTH-027',
        message: 'Nhập sai OTP quá nhiều lần',
    },
    AUTH_OTP_NOT_FOUND: {
        code: 'SYS-AUTH-028',
        message: 'Mã OTP không tồn tại hoặc đã bị sử dụng',
    },

    // ==== 2FA ====
    AUTH_2FA_REQUIRED: {
        code: 'SYS-AUTH-029',
        message: 'Yêu cầu xác thực hai bước',
    },
    AUTH_2FA_FAILED: {
        code: 'SYS-AUTH-030',
        message: 'Xác thực hai bước thất bại. Vui lòng thử lại mật khẩu',
    },

    // ==== SYSTEM ====
    SYSTEM_UNKNOWN_ERROR: {
        code: 'SYS-SYSTEM-000',
        message: 'Lỗi không xác định trong hệ thống',
    },
    SYS_INTERNAL_SERVER_ERROR: {
        code: 'SYS-SYSTEM-001',
        message: 'Lỗi không xác định từ phía server',
    },
    SYS_SERVICE_UNAVAILABLE: {
        code: 'SYS-SYSTEM-002',
        message: 'Dịch vụ tạm thời không khả dụng',
    },
    SYS_TIMEOUT: {
        code: 'SYS-SYSTEM-003',
        message: 'Request bị timeout',
    },
    SYS_DB_CONNECTION_FAILED: {
        code: 'SYS-SYSTEM-004',
        message: 'Không thể kết nối tới cơ sở dữ liệu',
    },
    SYS_CONFIGURATION_ERROR: {
        code: 'SYS-SYSTEM-005',
        message: 'Lỗi cấu hình hệ thống',
    },

    // ==== API ====
    API_NOT_FOUND: {
        code: 'SYS-API-001',
        message: 'API không tồn tại',
    },
    API_METHOD_NOT_ALLOWED: {
        code: 'SYS-API-002',
        message: 'Phương thức HTTP không được hỗ trợ',
    },
    API_UNSUPPORTED_MEDIA_TYPE: {
        code: 'SYS-API-003',
        message: 'Loại dữ liệu không được hỗ trợ (Content-Type)',
    },
    API_NOT_ACCEPTABLE: {
        code: 'SYS-API-004',
        message: 'Không chấp nhận loại phản hồi (Accept Header)',
    },
    API_BAD_REQUEST: {
        code: 'SYS-API-005',
        message: 'Request không hợp lệ',
    },

    // ==== VALIDATION ====
    SYS_VALIDATION_ERROR: {
        code: 'SYS-VALID-001',
        message: 'Dữ liệu đầu vào không hợp lệ',
    },
    SYS_MISSING_REQUIRED_FIELD: {
        code: 'SYS-VALID-002',
        message: 'Thiếu trường bắt buộc',
    },
    SYS_INVALID_FORMAT: {
        code: 'SYS-VALID-003',
        message: 'Định dạng không hợp lệ',
    },
    SYS_CONSTRAINT_VIOLATION: {
        code: 'SYS-VALID-004',
        message: 'Vi phạm ràng buộc dữ liệu',
    },

    // ==== FILE ====
    SYS_FILE_TOO_LARGE: {
        code: 'SYS-FILE-001',
        message: 'File vượt quá dung lượng cho phép',
    },
    SYS_FILE_UNSUPPORTED_TYPE: {
        code: 'SYS-FILE-002',
        message: 'File không đúng định dạng cho phép',
    },
    SYS_FILE_UPLOAD_FAILED: {
        code: 'SYS-FILE-003',
        message: 'Lỗi trong quá trình upload file',
    },
    SYS_FILE_NOT_FOUND: {
        code: 'SYS-FILE-004',
        message: 'Không tìm thấy file yêu cầu',
    },
} as const;



// ✅ Hàm tra cứu message theo code
export function getMessageByCode(code: string): string {
    for (const key in SystemErrorCode) {
        if (SystemErrorCode[key as keyof typeof SystemErrorCode].code === code) {
            return SystemErrorCode[key as keyof typeof SystemErrorCode].message;
        }
    }
    return 'Lỗi không xác định';
}