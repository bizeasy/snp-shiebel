import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { ContactMechPurposeService } from 'app/entities/contact-mech-purpose/service/contact-mech-purpose.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderContactMech } from '../order-contact-mech.model';
import { OrderContactMechService } from '../service/order-contact-mech.service';

import { OrderContactMechFormGroup, OrderContactMechFormService } from './order-contact-mech-form.service';

@Component({
  selector: 'sys-order-contact-mech-update',
  templateUrl: './order-contact-mech-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderContactMechUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderContactMech: IOrderContactMech | null = null;

  ordersSharedCollection = signal<IOrder[]>([]);
  contactMechesSharedCollection = signal<IContactMech[]>([]);
  contactMechPurposesSharedCollection = signal<IContactMechPurpose[]>([]);

  protected orderContactMechService = inject(OrderContactMechService);
  protected orderContactMechFormService = inject(OrderContactMechFormService);
  protected orderService = inject(OrderService);
  protected contactMechService = inject(ContactMechService);
  protected contactMechPurposeService = inject(ContactMechPurposeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderContactMechFormGroup = this.orderContactMechFormService.createOrderContactMechFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareContactMech = (o1: IContactMech | null, o2: IContactMech | null): boolean => this.contactMechService.compareContactMech(o1, o2);

  compareContactMechPurpose = (o1: IContactMechPurpose | null, o2: IContactMechPurpose | null): boolean =>
    this.contactMechPurposeService.compareContactMechPurpose(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderContactMech }) => {
      this.orderContactMech = orderContactMech;
      if (orderContactMech) {
        this.updateForm(orderContactMech);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderContactMech = this.orderContactMechFormService.getOrderContactMech(this.editForm);
    if (orderContactMech.id === null) {
      this.subscribeToSaveResponse(this.orderContactMechService.create(orderContactMech));
    } else {
      this.subscribeToSaveResponse(this.orderContactMechService.update(orderContactMech));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderContactMech | null>): void {
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

  protected updateForm(orderContactMech: IOrderContactMech): void {
    this.orderContactMech = orderContactMech;
    this.orderContactMechFormService.resetForm(this.editForm, orderContactMech);

    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, orderContactMech.order));
    this.contactMechesSharedCollection.update(contactMeches =>
      this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, orderContactMech.contactMech),
    );
    this.contactMechPurposesSharedCollection.update(contactMechPurposes =>
      this.contactMechPurposeService.addContactMechPurposeToCollectionIfMissing<IContactMechPurpose>(
        contactMechPurposes,
        orderContactMech.contactMechPurpose,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.orderContactMech?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.contactMechService
      .query()
      .pipe(map((res: HttpResponse<IContactMech[]>) => res.body ?? []))
      .pipe(
        map((contactMeches: IContactMech[]) =>
          this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, this.orderContactMech?.contactMech),
        ),
      )
      .subscribe((contactMeches: IContactMech[]) => this.contactMechesSharedCollection.set(contactMeches));

    this.contactMechPurposeService
      .query()
      .pipe(map((res: HttpResponse<IContactMechPurpose[]>) => res.body ?? []))
      .pipe(
        map((contactMechPurposes: IContactMechPurpose[]) =>
          this.contactMechPurposeService.addContactMechPurposeToCollectionIfMissing<IContactMechPurpose>(
            contactMechPurposes,
            this.orderContactMech?.contactMechPurpose,
          ),
        ),
      )
      .subscribe((contactMechPurposes: IContactMechPurpose[]) => this.contactMechPurposesSharedCollection.set(contactMechPurposes));
  }
}
