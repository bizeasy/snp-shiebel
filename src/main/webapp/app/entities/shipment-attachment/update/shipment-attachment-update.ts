import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShipmentAttachmentService } from '../service/shipment-attachment.service';
import { IShipmentAttachment } from '../shipment-attachment.model';

import { ShipmentAttachmentFormGroup, ShipmentAttachmentFormService } from './shipment-attachment-form.service';

@Component({
  selector: 'sys-shipment-attachment-update',
  templateUrl: './shipment-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShipmentAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  shipmentAttachment: IShipmentAttachment | null = null;

  shipmentsSharedCollection = signal<IShipment[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected shipmentAttachmentService = inject(ShipmentAttachmentService);
  protected shipmentAttachmentFormService = inject(ShipmentAttachmentFormService);
  protected shipmentService = inject(ShipmentService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipmentAttachmentFormGroup = this.shipmentAttachmentFormService.createShipmentAttachmentFormGroup();

  compareShipment = (o1: IShipment | null, o2: IShipment | null): boolean => this.shipmentService.compareShipment(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentAttachment }) => {
      this.shipmentAttachment = shipmentAttachment;
      if (shipmentAttachment) {
        this.updateForm(shipmentAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shipmentAttachment = this.shipmentAttachmentFormService.getShipmentAttachment(this.editForm);
    if (shipmentAttachment.id === null) {
      this.subscribeToSaveResponse(this.shipmentAttachmentService.create(shipmentAttachment));
    } else {
      this.subscribeToSaveResponse(this.shipmentAttachmentService.update(shipmentAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShipmentAttachment | null>): void {
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

  protected updateForm(shipmentAttachment: IShipmentAttachment): void {
    this.shipmentAttachment = shipmentAttachment;
    this.shipmentAttachmentFormService.resetForm(this.editForm, shipmentAttachment);

    this.shipmentsSharedCollection.update(shipments =>
      this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, shipmentAttachment.shipment),
    );
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, shipmentAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shipmentService
      .query()
      .pipe(map((res: HttpResponse<IShipment[]>) => res.body ?? []))
      .pipe(
        map((shipments: IShipment[]) =>
          this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, this.shipmentAttachment?.shipment),
        ),
      )
      .subscribe((shipments: IShipment[]) => this.shipmentsSharedCollection.set(shipments));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.shipmentAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
