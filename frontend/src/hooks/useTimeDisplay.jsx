import {useEffect, useRef, useState} from "react";

export function useTimeDisplay(duration = 5000) {
    const [display, setDisplay] = useState('none');
    const [error, setError] = useState('');
    const timer = useRef(0);

    const showMessage = (msg) => {
        setError(msg);
        setDisplay('block');
    }

    useEffect(() => {
            timer.current = setTimeout(() => {
                setDisplay('none');
            }, 5000)

        return () => clearTimeout(timer.current);
    }, [display])

    return {error, display, showMessage};
}