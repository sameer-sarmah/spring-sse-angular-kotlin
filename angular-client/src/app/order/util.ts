export function formatDate(timeStr: string) {
    const options = {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        minute: '2-digit',
        hour: '2-digit'
    };
    let timeLong = Date.now();
    try {
        timeLong = Date.parse(timeStr);
    } catch (error) {
        console.error(
            `${timeStr} is a invalid date`
        );
    }
    const time = new Date(timeLong);
    return time.toLocaleDateString('en', options);
}
