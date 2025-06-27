import SubmitButton from "./SubmitButton";

const HolidayAddCheckForm = ({onSubmit, addHoliday, getHoliday, startDate, setStartDate, endDate, setEndDate}) => {
    return <form id="holiday" onSubmit={onSubmit}>
        <label htmlFor="start-date" id="label-start-date"><b>Start Date:</b></label>
        <label htmlFor="endDate" id="label-end-date"><b>End Date:</b></label>
        <input type="datetime-local" id="start-date" name="start-date" value={startDate}
               onChange={e => setStartDate(e.target.value)} required/>
        <input type="datetime-local" id="end-date" name="end-date" value={endDate}
               onChange={e => setEndDate(e.target.value)} required/>
        <SubmitButton id="add-holidays" onClick={addHoliday}>Add Holiday</SubmitButton>
        <SubmitButton id="get-holidays" onClick={getHoliday}>Get Holiday</SubmitButton>
    </form>
}

export default HolidayAddCheckForm;
