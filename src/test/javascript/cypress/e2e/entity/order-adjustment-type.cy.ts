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

describe('OrderAdjustmentType e2e test', () => {
  const orderAdjustmentTypePageUrl = '/order-adjustment-type';
  let username: string;
  let password: string;
  const orderAdjustmentTypeSample = { name: 'sell custom' };

  let orderAdjustmentType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-adjustment-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-adjustment-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-adjustment-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orderAdjustmentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-adjustment-types/${orderAdjustmentType.id}`,
      }).then(() => {
        orderAdjustmentType = undefined;
      });
    }
  });

  it('OrderAdjustmentTypes menu should load OrderAdjustmentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-adjustment-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderAdjustmentType').should('exist');
    cy.location('pathname').should('eq', orderAdjustmentTypePageUrl);
  });

  describe('OrderAdjustmentType page', () => {
    it('should have translated page title', () => {
      cy.visit(orderAdjustmentTypePageUrl);
      cy.getEntityHeading('OrderAdjustmentType').should('not.contain', 'snpApp.orderAdjustmentType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderAdjustmentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderAdjustmentType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderAdjustmentTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderAdjustmentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-adjustment-types',
          body: orderAdjustmentTypeSample,
        }).then(({ body }) => {
          orderAdjustmentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-adjustment-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [orderAdjustmentType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderAdjustmentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderAdjustmentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderAdjustmentType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentTypePageUrl);
      });

      it('edit button click should load edit OrderAdjustmentType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderAdjustmentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentTypePageUrl);
      });

      it('edit button click should load edit OrderAdjustmentType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderAdjustmentType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentTypePageUrl);
      });

      it('last delete button click should delete instance of OrderAdjustmentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderAdjustmentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAdjustmentTypePageUrl);

        orderAdjustmentType = undefined;
      });
    });
  });

  describe('new OrderAdjustmentType page', () => {
    beforeEach(() => {
      cy.visit(orderAdjustmentTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderAdjustmentType');
    });

    it('should create an instance of OrderAdjustmentType', () => {
      cy.get(`[data-cy="name"]`).type('safely pish');
      cy.get(`[data-cy="name"]`).should('have.value', 'safely pish');

      cy.get(`[data-cy="description"]`).type('knavishly');
      cy.get(`[data-cy="description"]`).should('have.value', 'knavishly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderAdjustmentType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderAdjustmentTypePageUrl);
    });
  });
});
