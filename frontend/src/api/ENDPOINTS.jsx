const API_BASE_URL = 'http://localhost:8080';

export const ENDPOINTS = {
    USER_BASE: `${API_BASE_URL}/users`,
    USER_FILTER: `${API_BASE_URL}/users/filter`,
    USER_UPDATE: `${API_BASE_URL}/users/update`,
    ADD_HOLIDAYS: `${API_BASE_URL}/holidays`,
    CHANGE_EMAIL: `${API_BASE_URL}/users/in/email`,
    CHANGE_PASSWORD: `${API_BASE_URL}/users/in/password`,
    LOST_PASSWORD: `${API_BASE_URL}/users/lost`,
    NEW_PASSWORD: `${API_BASE_URL}/users/new`,
    GET_USER_DATA: `${API_BASE_URL}/users/in/user`,
    FILTER_HOLIDAYS: `${API_BASE_URL}/holidays/filter`,
    SAVE_GET_HOLIDAY: `${API_BASE_URL}/holidays?`,
    LOGIN: `${API_BASE_URL}/login`,
    GET_ROLE: `${API_BASE_URL}/users/in/role`,
    LOGOUT: `${API_BASE_URL}/logout`,
}