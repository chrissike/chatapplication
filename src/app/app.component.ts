import {Component} from '@angular/core';
import {Http} from "@angular/http";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    constructor(private http: Http) {

    }

    deleteAllData(): void {
        this.http.delete(`${ADMIN_SERVICE}/deleteAllData`).subscribe(
            res => {
                console.log(res.json());
                alert("All data was deleted!");
            },
            err => console.log(err)
        )
    }
}

export const ADMIN_SERVICE = "http://localhost:8089/chatapp/resources/admin";