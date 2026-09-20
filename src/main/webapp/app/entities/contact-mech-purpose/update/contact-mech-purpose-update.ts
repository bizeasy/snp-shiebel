import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContactMechPurpose } from '../contact-mech-purpose.model';
import { ContactMechPurposeService } from '../service/contact-mech-purpose.service';

import { ContactMechPurposeFormGroup, ContactMechPurposeFormService } from './contact-mech-purpose-form.service';

@Component({
  selector: 'sys-contact-mech-purpose-update',
  templateUrl: './contact-mech-purpose-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ContactMechPurposeUpdate implements OnInit {
  readonly isSaving = signal(false);
  contactMechPurpose: IContactMechPurpose | null = null;

  protected contactMechPurposeService = inject(ContactMechPurposeService);
  protected contactMechPurposeFormService = inject(ContactMechPurposeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContactMechPurposeFormGroup = this.contactMechPurposeFormService.createContactMechPurposeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactMechPurpose }) => {
      this.contactMechPurpose = contactMechPurpose;
      if (contactMechPurpose) {
        this.updateForm(contactMechPurpose);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const contactMechPurpose = this.contactMechPurposeFormService.getContactMechPurpose(this.editForm);
    if (contactMechPurpose.id === null) {
      this.subscribeToSaveResponse(this.contactMechPurposeService.create(contactMechPurpose));
    } else {
      this.subscribeToSaveResponse(this.contactMechPurposeService.update(contactMechPurpose));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IContactMechPurpose | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(contactMechPurpose: IContactMechPurpose): void {
    this.contactMechPurpose = contactMechPurpose;
    this.contactMechPurposeFormService.resetForm(this.editForm, contactMechPurpose);
  }
}
