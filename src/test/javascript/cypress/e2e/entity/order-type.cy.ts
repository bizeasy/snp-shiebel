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

describe('OrderType e2e test', () => {
  const orderTypePageUrl = '/order-type';
  let username: string;
  let password: string;
  const orderTypeSample = { name: 'queasily across' };

  let orderType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orderType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-types/${orderType.id}`,
      }).then(() => {
        orderType = undefined;
      });
    }
  });

  it('OrderTypes menu should load OrderTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderType').should('exist');
    cy.location('pathname').should('eq', orderTypePageUrl);
  });

  describe('OrderType page', () => {
    it('should have translated page title', () => {
      cy.visit(orderTypePageUrl);
      cy.getEntityHeading('OrderType').should('not.contain', 'snpApp.orderType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-types',
          body: orderTypeSample,
        }).then(({ body }) => {
          orderType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [orderType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderTypePageUrl);
      });

      it('edit button click should load edit OrderType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderTypePageUrl);
      });

      it('edit button click should load edit OrderType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderTypePageUrl);
      });

      it('last delete button click should delete instance of OrderType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderTypePageUrl);

        orderType = undefined;
      });
    });
  });

  describe('new OrderType page', () => {
    beforeEach(() => {
      cy.visit(orderTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderType');
    });

    it('should create an instance of OrderType', () => {
      cy.get(`[data-cy="name"]`).type('woefully');
      cy.get(`[data-cy="name"]`).should('have.value', 'woefully');

      cy.get(`[data-cy="description"]`).type('natural underneath');
      cy.get(`[data-cy="description"]`).should('have.value', 'natural underneath');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderTypePageUrl);
    });
  });
});
