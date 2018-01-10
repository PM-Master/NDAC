import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Response} from './Response';
import {Injectable} from '@angular/core';

@Injectable()
export class PmHealthService {
  headers = {
    headers: new HttpHeaders().set('Content-Type', 'application/json'),
  };
  private authUrl = 'http://localhost:8080/pmhealth/api/auth';
  private linksUrl = 'http://localhost:8080/pmhealth/api/links';
  private recordsUrl = 'http://localhost:8080/pmhealth/api/records';
  private visitsUrl = 'http://localhost:8080/pmhealth/api/visits';
  private sessionsUrl = 'http://localhost:8080/pmhealth/api/sessions';
  private medsUrl = 'http://localhost:8080/pmhealth/api/medicines';

  private sessionId;

  constructor(private http: HttpClient) { }

  public login(username: string, password: string): Promise<string> {
    const data = {
      'username': username,
      'password': password
    };

    return this.http
      .post(this.authUrl, data, this.headers)
      .toPromise()
      .then(response => {
        console.log(response['entity']);
        const sessionId = response['entity'];
        if (sessionId != null) {
          localStorage.setItem('SESSION_ID', sessionId);
          return sessionId;
        } else {
          return null;
        }
      });
  }

  private getSessionId(): string {
    console.log(localStorage.getItem('SESSION_ID'));
    return localStorage.getItem('SESSION_ID');
  }

  public getDashboard(): Promise<any> {
    const url = `${this.linksUrl}?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        console.log(response['entity']);
        return response['entity'];
      });
  }

  public getPatients(): Promise<any> {
    const url = `${this.recordsUrl}?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        console.log(response['entity']);
        return response['entity'];
      });
  }

  public getRecord(patientId: number): Promise<any> {
    const url = `${this.recordsUrl}/${patientId}?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        console.log(response);
      });
  }

  public getInfo(patientId: number): Promise<any> {
    const url = `${this.recordsUrl}/${patientId}?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        return response['entity'];
      });
  }

  public getVisits(patientId: number): Promise<any> {
    const url = `${this.recordsUrl}/${patientId}/visits?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        return response['entity'];
      });
  }

  public getAllVisits(): Promise<any> {
    const url = `${this.recordsUrl}/visits?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        return response['entity'];
      });
  }

  public getVitals(visitId: number): Promise<any> {
    const url = `${this.visitsUrl}/${visitId}/vitals?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        return response['entity'];
      });
  }

  public getLatestVitals(patientId: number): Promise<any> {
    const url = `${this.recordsUrl}/${patientId}/vitals?sessionId=${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        return response['entity'];
      });
  }

  public updateInfo(patientId: number, data: any): Promise<any> {
    const url = `${this.recordsUrl}/${patientId}?sessionId=${this.getSessionId()}`;
    return this.http.put(url, data)
      .toPromise()
      .then(response => response['entity']);
  }
  public updateVisit(patientId: number, visit: any): Promise<any> {
    console.log(visit);

    const url = `${this.recordsUrl}/${patientId}/visits/${visit.visitId}?sessionId=${this.getSessionId()}`;
    return this.http.put(url, visit)
      .toPromise()
      .then(response => {
        console.log(response['entity']);
        return response['entity'];
      });
  }

  public getSessionPatientId(): Promise<number> {
    const url = `${this.sessionsUrl}/${this.getSessionId()}`;
    console.log(url);
    return this.http.get(url)
      .toPromise()
      .then(response => {
        console.log(response);
        return response['entity'].patientId;
      });
  }

  public startVisit(patientId: number): Promise<any> {
    const url = `${this.recordsUrl}/${patientId}/visits?${this.getSessionId()}`;
    return this.http.post(url, null)
      .toPromise()
      .then(response => {
        console.log(response);
        return response['entity'];
      });
  }

  getMedications() {
    const url = `${this.medsUrl}?${this.getSessionId()}`;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        console.log(response);
        return response['entity'];
      });
  }
}
