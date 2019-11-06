export class CoreException {
    constructor(private status: number, private textStatus: string, private errorThrown: string) {}
    getTextStatus() {
        return this.textStatus;
    }
    getError() {
        return this.errorThrown;
    }

    getStatusCode() {
        return this.status;
    }
}
