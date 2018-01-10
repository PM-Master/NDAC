import {Component, HostListener, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {PmHealthService} from '../services/pmhealth.service';
import {AlertService} from '../services/alert.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-patient-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  patientId: number;
  currentVisit: any = {};
  oldCurrentVisit: any = {};

  infoModel: any = {};
  oldInfoModel: any = {};
  visitList: any = [];
  vitalsModel: any = null;
  diagnosisList: any = [];
  prescriptions: any = [];

  session: any;

  constructor(private route: ActivatedRoute,
              private pmhealth: PmHealthService,
              private alertService: AlertService) {
    this.currentVisit.vitals = {};
    this.oldCurrentVisit.vitals = {};
  }

  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          this.patientId = +params['patientId'];

          this.route.queryParams
            .subscribe(
              (qParams) => {
                this.session = qParams['session'];
                this.getRecord();
              });
        });
  }

  getRecord() {
    // get info
    this.pmhealth.getInfo(this.patientId)
      .then(response => {
        this.infoModel = response;
        this.setOldInfoModel();
      })
      .catch(console.log);

    // get visits
    this.pmhealth.getVisits(this.patientId)
      .then(response => {
        response = response.reverse();
        this.visitList = [];
        for (const v of response) {
          if (v.admissionDate !== null) {
            this.visitList.push(v);
          }
        }
        console.log(this.visitList);
        this.visitList = this.visitList.sort((a, b) => {
          return a.visitId - b.visitId;
        });

        // get last visit with vitals to display in summary
        let x = this.visitList.length - 1;
        while (this.vitalsModel == null && x > 0) {
          this.vitalsModel = this.visitList[x].vitals;
          x--;
        }

        // create list of all diagnoses
        for (const v of this.visitList) {
          if (v.diagnoses !== null) {
            this.diagnosisList.push({'diagnoses': v.diagnoses, 'visit': v.visitId});
          }
        }

        // get all prescriptions
        console.log(this.visitList);
        for (const visit of this.visitList) {
          if (visit.prescription !== null) {
            if (visit.prescription.name !== null) {
              this.prescriptions.push(visit.prescription.name);
            }
          }
        }
        console.log('DONE');
      })
      .catch(console.log);
  }

  validVitals(vitals: any): boolean {
    return (vitals === null) ? false : vitals.height > 0 &&
      vitals.weight > 0 &&
      vitals.temperature > 0 &&
      vitals.pulse > 0 &&
      vitals.bloodPressure !== null;
  }

  /**
   * Sets the current visit.
   * @param visit the visit selected from the list
   */
  setCurrentVisit(visit: any) {
    this.currentVisit = visit;
    console.log(this.currentVisit);
    this.oldCurrentVisit = {};
    this.oldCurrentVisit.vitals = {};
    this.oldCurrentVisit.prescription = {};
    this.oldCurrentVisit.admissionDate = this.currentVisit.admissionDate;
    this.oldCurrentVisit.dischargeDate = this.currentVisit.dischargeDate;
    this.oldCurrentVisit.reason = this.currentVisit.reason;

    // instantiate the current vitals and prescription if there are none
    if (this.currentVisit.vitals == null) {
      this.currentVisit.vitals = {};
    }
    if (this.currentVisit.prescription == null) {
      this.currentVisit.prescription = {};
    }
    this.oldCurrentVisit.vitals.height = this.currentVisit.vitals.height;
    this.oldCurrentVisit.vitals.weight = this.currentVisit.vitals.weight;
    this.oldCurrentVisit.vitals.temperature = this.currentVisit.vitals.temperature;
    this.oldCurrentVisit.vitals.pulse = this.currentVisit.vitals.pulse;
    this.oldCurrentVisit.vitals.bloodPressure = this.currentVisit.vitals.bloodPressure;
    this.oldCurrentVisit.diagnoses = this.currentVisit.diagnoses;
    this.oldCurrentVisit.treatments = this.currentVisit.treatments;
    this.oldCurrentVisit.notes = this.currentVisit.notes;
    this.oldCurrentVisit.prescription.name = this.currentVisit.prescription.name;
    this.oldCurrentVisit.prescription.dosage = this.currentVisit.prescription.dosage;
    this.oldCurrentVisit.prescription.duration = this.currentVisit.prescription.duration;
    console.log(this.currentVisit.notes);
  }

  /**
   * Update the patient info
   */
  updateInfo() {
    const data: any = {};
    data.vitals = {};
    if (this.infoModel.name !== this.oldInfoModel.name) {
      data.name = this.infoModel.name;
    }
    if (this.infoModel.dob !== this.oldInfoModel.dob) {
      data.dob = this.infoModel.dob;
    }
    if (this.infoModel.ssn !== this.oldInfoModel.ssn) {
      data.ssn = this.infoModel.ssn;
    }
    if (this.infoModel.gender !== this.oldInfoModel.gender) {
      data.gender = this.infoModel.gender;
    }
    if (this.infoModel.race !== this.oldInfoModel.race) {
      data.race = this.infoModel.race;
    }
    if (this.infoModel.maritalStatus !== this.oldInfoModel.maritalStatus) {
      data.maritalStatus = this.infoModel.maritalStatus;
    }
    if (this.infoModel.cellPhone !== this.oldInfoModel.cellPhone) {
      data.cellPhone = this.infoModel.cellPhone;
    }
    if (this.infoModel.workPhone !== this.oldInfoModel.workPhone) {
      data.workPhone = this.infoModel.workPhone;
    }
    if (this.infoModel.homePhone !== this.oldInfoModel.homePhone) {
      data.homePhone = this.infoModel.homePhone;
    }
    if (this.infoModel.email !== this.oldInfoModel.email) {
      data.email = this.infoModel.email;
    }
    if (this.infoModel.address !== this.oldInfoModel.address) {
      data.address = this.infoModel.address;
    }

    this.pmhealth.updateInfo(this.patientId, data)
      .then(response => {
        if (response === null) {
          this.alertService.error('An error occurred while updating patient info');
        } else {
          this.alertService.success('Successfully updated patient record');
          this.setOldInfoModel();
        }
      });
  }

  /**
   * sets the old info model which is used to detect changes in the values
   */
  setOldInfoModel() {
    this.oldInfoModel = {};
    this.oldInfoModel.name = this.infoModel.name;
    this.oldInfoModel.dob = this.infoModel.dob;
    this.oldInfoModel.ssn = this.infoModel.ssn;
    this.oldInfoModel.gender = this.infoModel.gender;
    this.oldInfoModel.maritalStatus = this.infoModel.maritalStatus;
    this.oldInfoModel.cellPhone = this.infoModel.cellPhone;
    this.oldInfoModel.workPhone = this.infoModel.workPhone;
    this.oldInfoModel.homePhone = this.infoModel.homePhone;
    this.oldInfoModel.email = this.infoModel.email;
    this.oldInfoModel.address = this.infoModel.address;
  }

  /**
   * Update the current visit
   */
  updateVisit() {
    const data: any = {};
    data.visitId = this.currentVisit.visitId;
    data.vitals = {};
    data.prescription = {};
    if (this.currentVisit.admissionDate !== this.oldCurrentVisit.admissionDate) {
      data.admissionDate = this.currentVisit.admissionDate;
    }
    if (this.currentVisit.dischareDate !== this.oldCurrentVisit.dischareDate) {
      data.dischareDate = this.currentVisit.dischareDate;
    }
    if (this.currentVisit.vitals.height !== this.oldCurrentVisit.vitals.height) {
      data.vitals.height = this.currentVisit.vitals.height;
    }
    if (this.currentVisit.vitals.weight !== this.oldCurrentVisit.vitals.weight) {
      data.vitals.weight = this.currentVisit.vitals.weight;
    }
    if (this.currentVisit.vitals.temperature !== this.oldCurrentVisit.vitals.temperature) {
      data.vitals.temperature = this.currentVisit.vitals.temperature;
    }
    if (this.currentVisit.vitals.pulse !== this.oldCurrentVisit.vitals.pulse) {
      data.vitals.pulse = this.currentVisit.vitals.pulse;
    }
    if (this.currentVisit.vitals.bloodPressure !== this.oldCurrentVisit.vitals.bloodPressure) {
      data.vitals.bloodPressure = this.currentVisit.vitals.bloodPressure;
    }
    if (this.currentVisit.diagnoses !== this.oldCurrentVisit.diagnoses) {
      data.diagnoses = this.currentVisit.diagnoses;
    }
    if (this.currentVisit.treatments !== this.oldCurrentVisit.treatments) {
      data.treatments = this.currentVisit.treatments;
    }
    if (this.currentVisit.notes !== this.oldCurrentVisit.notes) {
      data.notes = this.currentVisit.notes;
    }
    if (this.currentVisit.prescription.name !== this.oldCurrentVisit.prescription.name) {
      data.prescription.name = this.currentVisit.prescription.name;
    }
    if (this.currentVisit.prescription.dosage !== this.oldCurrentVisit.prescription.dosage) {
      data.prescription.dosage = this.currentVisit.prescription.dosage;
    }
    if (this.currentVisit.prescription.duration !== this.oldCurrentVisit.prescription.duration) {
      data.prescription.duration = this.currentVisit.prescription.duration;
    }
    console.log(data);
    this.pmhealth.updateVisit(this.patientId, data)
      .then(response => {
        if (response === null) {
          this.alertService.error('An error occurred while updating patient info');
        } else {
          this.currentVisit = response;
          this.alertService.success('Successfully updated patient record');
        }
      });
  }

  /**
   * End the visit.  Only need to send the patientId, visitId,
   * and true, telling the server to include a discharge date
   *
   */
  endVisit() {
    const endVisit: any = {};
    endVisit.visitId = this.currentVisit.visitId;
    endVisit.dischargeDate = 'true';

    this.pmhealth.updateVisit(this.patientId, endVisit)
      .then(response => {
        if (response === null) {
          this.alertService.error('An error occurred while ending the visit');
        } else {
          this.currentVisit = response;
          console.log(this.currentVisit);
          this.setCurrentVisit(response);
          this.alertService.success('Visit ended.');
          console.log(this.currentVisit.dischargeDate == null);

          // refresh the visit list
          let x = 0;
          for (const v of this.visitList) {
            if (v.visitId === this.currentVisit.visitId) {
              this.visitList[x] = response;
            } else {
              x++;
            }
          }
        }
      });
  }
}
