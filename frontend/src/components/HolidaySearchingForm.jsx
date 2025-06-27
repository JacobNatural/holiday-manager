import SubmitButton from "./SubmitButton";

const HolidaySearchingForm = ({onSubmit}) => {
    return <form id="holiday-searching-form" onSubmit={onSubmit}>
        <label htmlFor="userId" id="labelUserIdHolidayManager">User id:</label>
        <input type="text" id="userIdHolidayManager" name="userId" className="gridAreaSpace"/>

        <label htmlFor="holidayId" id="labelHolidayIdHolidayManager">Holiday id:</label>
        <input type="text" id="holidayIdHolidayManager" name="holidayId" className="gridAreaSpace"/>

        <label htmlFor="startDate" id="labelStartDateHolidayManager">Start date:</label>
        <input type="datetime-local" id="startDateHolidayManager" name="startDate" className="gridAreaSpace"/>

        <label htmlFor="endDate" id="labelEndDateHolidayManager">Start date:</label>
        <input type="datetime-local" id="endDateHolidayManager" name="endDate" className="gridAreaSpace"/>

        <label htmlFor="status" id="labelStatusHolidayManager">Status:</label>
        <select name="status" id="statusSelectHolidayManager">
            <option></option>
            <option value="REJECTED">REJECTED</option>
            <option value="PROCESSING">PROCESSING</option>
            <option value="ACCEPTED">ACCEPTED</option>
        </select>
        <SubmitButton id={"save-button"}>search</SubmitButton>
    </form>
}

export default HolidaySearchingForm;