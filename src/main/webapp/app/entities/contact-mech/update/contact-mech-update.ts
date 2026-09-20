import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IContactMechType } from 'app/entities/contact-mech-type/contact-mech-type.model';
import { ContactMechTypeService } from 'app/entities/contact-mech-type/service/contact-mech-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContactMech } from '../contact-mech.model';
import { ContactMechService } from '../service/contact-mech.service';

import { ContactMechFormGroup, ContactMechFormService } from './contact-mech-form.service';

@Component({
  selector: 'sys-contact-mech-update',
  templateUrl: './contact-mech-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ContactMechUpdate implements OnInit {
  readonly isSaving = signal(false);
  contactMech: IContactMech | null = null;

  contactMechTypesSharedCollection = signal<IContactMechType[]>([]);

  protected contactMechService = inject(ContactMechService);
  protected contactMechFormService = inject(ContactMechFormService);
  protected contactMechTypeService = inject(ContactMechTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContactMechFormGroup = this.contactMechFormService.createContactMechFormGroup();

  compareContactMechType = (o1: IContactMechType | null, o2: IContactMechType | null): boolean =>
    this.contactMechTypeService.compareContactMechType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactMech }) => {
      this.contactMech = contactMech;
      if (contactMech) {
        this.updateForm(contactMech);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const contactMech = this.contactMechFormService.getContactMech(this.editForm);
    if (contactMech.id === null) {
      this.subscribeToSaveResponse(this.contactMechService.create(contactMech));
    } else {
      this.subscribeToSaveResponse(this.contactMechService.update(contactMech));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IContactMech | null>): void {
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

  protected updateForm(contactMech: IContactMech): void {
    this.contactMech = contactMech;
    this.contactMechFormService.resetForm(this.editForm, contactMech);

    this.contactMechTypesSharedCollection.update(contactMechTypes =>
      this.contactMechTypeService.addContactMechTypeToCollectionIfMissing<IContactMechType>(contactMechTypes, contactMech.contactMechType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactMechTypeService
      .query()
      .pipe(map((res: HttpResponse<IContactMechType[]>) => res.body ?? []))
      .pipe(
        map((contactMechTypes: IContactMechType[]) =>
          this.contactMechTypeService.addContactMechTypeToCollectionIfMissing<IContactMechType>(
            contactMechTypes,
            this.contactMech?.contactMechType,
          ),
        ),
      )
      .subscribe((contactMechTypes: IContactMechType[]) => this.contactMechTypesSharedCollection.set(contactMechTypes));
  }
}
