import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('OrderAdjustment e2e test', () => {
  const orderAdjustmentPageUrl = '/order-adjustment';
  let username: string;
  let password: string;
  const orderAdjustmentSample = {};

  let orderAdjustment;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-adjustments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-adjustments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-adjustments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orderAdjustment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-adjustments/${orderAdjustment.id}`,
      }).then(() => {
        orderAdjustment = undefined;
      });
    }
  });

  it('OrderAdjustments menu should load OrderAdjustments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-adjustment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderAdjustment').should('exist');
    cy.location('pathname').should('eq', orderAdjustmentPageUrl);
  });

  describe('OrderAdjustment page', () => {
    it('should have translated page title', () => {
      cy.visit(orderAdjustmentPageUrl);
      cy.getEntityHeading('OrderAdjustment').should('not.contain', 'snpApp.orderAdjustment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderAdjustmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderAdjustment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderAdjustmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderAdjustment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-adjustments',
          body: orderAdjustmentSample,
        }).then(({ body }) => {
          orderAdjustment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-adjustments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-adjustments?page=0&size=20>; rel="last",<http://localhost/api/order-adjustments?page=0&size=20>; rel="first"',
              },
              body: [orderAdjustment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderAdjustmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderAdjustment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderAdjustment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentPageUrl);
      });

      it('edit button click should load edit OrderAdjustment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderAdjustment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentPageUrl);
      });

      it('edit button click should load edit OrderAdjustment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderAdjustment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentPageUrl);
      });

      it('last delete button click should delete instance of OrderAdjustment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderAdjustment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentPageUrl);

        orderAdjustment = undefined;
      });
    });
  });

  describe('new OrderAdjustment page', () => {
    beforeEach(() => {
      cy.visit(orderAdjustmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderAdjustment');
    });

    it('should create an instance of OrderAdjustment', () => {
      cy.get(`[data-cy="description"]`).type('safeguard absent rule');
      cy.get(`[data-cy="description"]`).should('have.value', 'safeguard absent rule');

      cy.get(`[data-cy="comments"]`).type('past yowza wrongly');
      cy.get(`[data-cy="comments"]`).should('have.value', 'past yowza wrongly');

      cy.get(`[data-cy="amount"]`).type('27018.64');
      cy.get(`[data-cy="amount"]`).should('have.value', '27018.64');

      cy.get(`[data-cy="sourcePercentage"]`).type('24114.25');
      cy.get(`[data-cy="sourcePercentage"]`).should('have.value', '24114.25');

      cy.get(`[data-cy="sourceRefId"]`).type('obediently fiercely before');
      cy.get(`[data-cy="sourceRefId"]`).should('have.value', 'obediently fiercely before');

      cy.get(`[data-cy="exemptAmount"]`).type('27502.96');
      cy.get(`[data-cy="exemptAmount"]`).should('have.value', '27502.96');

      cy.get(`[data-cy="includeInTax"]`).should('not.be.checked');
      cy.get(`[data-cy="includeInTax"]`).click();
      cy.get(`[data-cy="includeInTax"]`).should('be.checked');

      cy.get(`[data-cy="includeInShipping"]`).should('not.be.checked');
      cy.get(`[data-cy="includeInShipping"]`).click();
      cy.get(`[data-cy="includeInShipping"]`).should('be.checked');

      cy.get(`[data-cy="includedInPrice"]`).should('not.be.checked');
      cy.get(`[data-cy="includedInPrice"]`).click();
      cy.get(`[data-cy="includedInPrice"]`).should('be.checked');

      cy.get(`[data-cy="entryDate"]`).type('2023-12-14T00:59');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2023-12-14T00:59');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderAdjustment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderAdjustmentPageUrl);
    });
  });
});
