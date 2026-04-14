import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private transactions = [
    {
      "id": 1,
      "condidat_firstname": "nour",
      "condidat_lastname": "mejai",
      "process_name": "process1",
      "amount": 120,
      "status": "Valider"
    },
    {
      "id": 2,
      "condidat_firstname": "yasmine",
      "condidat_lastname": "Marouani",
      "process_name": "process2",
      "amount": 162,
      "status": "Valider"
    },
    {
      "id": 3,
      "condidat_firstname": "Samir",
      "condidat_lastname": "boudawara",
      "process_name": "process3",
      "amount": 172,
      "status": "En Cours"
    },
    {
      "id": 4,
      "condidat_firstname": "samia",
      "condidat_lastname": "benchikh",
      "process_name": "process4",
      "amount": 912,
      "status": "Refuser"
    },
    {
      "id": 5,
      "condidat_firstname": "slim",
      "condidat_lastname": "Dargachi",
      "process_name": "process5",
      "amount": 312,
      "status": "En Cours"
    },
    {
      "id": 6,
      "condidat_firstname": "Fathi",
      "condidat_lastname": "Kouched",
      "process_name": "process6",
      "amount": 124,
      "status": "Refuser"
    },
    {
      "id": 7,
      "condidat_firstname": "Imed",
      "condidat_lastname": "hajjar",
      "process_name": "process7",
      "amount": 152,
      "status": "Valider"
    },
    {
      "id": 8,
      "condidat_firstname": "Nooman",
      "condidat_lastname": "fehri",
      "process_name": "process8",
      "amount": 412,
      "status": "Valider"
    },
    {
      "id": 9,
      "condidat_firstname": "nadia",
      "condidat_lastname": "sehli",
      "process_name": "process9",
      "amount": 232,
      "status": "Valider"
    },
    {
      "id": 10,
      "condidat_firstname": "salma",
      "condidat_lastname": "ferchichi",
      "process_name": "process10",
      "amount": 168,
      "status": "Refuser"
    }
  ];

  constructor() {}

  getTransactions(): Observable<any[]> {
    return of(this.transactions);
  }

  getTransactionById(id: string): Observable<any> {
    const transaction = this.transactions.find(t => t.id.toString() === id);
    return of(transaction);
  }
}
