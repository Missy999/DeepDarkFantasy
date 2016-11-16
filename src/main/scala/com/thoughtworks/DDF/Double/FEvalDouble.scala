package com.thoughtworks.DDF.Double

import com.thoughtworks.DDF.Arrow.FEvalArr
import com.thoughtworks.DDF.Bool.FEvalBool
import com.thoughtworks.DDF.Gradient.Gradient
import com.thoughtworks.DDF.Language.{LangInfoG, LangTerm, LangTermLang, NextLang}
import com.thoughtworks.DDF.Product.FEvalProd
import com.thoughtworks.DDF.{FEval, FEvalCase, FEvalMatch}

trait FEvalDouble extends
  Double[FEvalCase, FEval] with
  FEvalArr with
  FEvalBool with
  FEvalProd {
  implicit val ltl = LangTermLang

  implicit val di = ltl.doubleInfo

  implicit def pi[A, B](implicit ai: LangInfoG[A], bi: LangInfoG[B]) = ltl.prodInfo(ai, bi)

  override def ltD =
    new FEval[scala.Double => scala.Double => Boolean] {
      override val fec = aInfo(doubleInfo, aInfo(doubleInfo, boolInfo))

      override def term[G: Gradient] =
        base.C_(base.B__(
          base.C_(base.B__(base.ltD)(base.zro(base.doubleInfo, implicitly[Gradient[G]].GInfo)))
        )(base.zro(base.doubleInfo, implicitly[Gradient[G]].GInfo)))
    }

  override def divD = new FEval[scala.Double => scala.Double => scala.Double] {
    override val fec = aInfo(doubleInfo, aInfo(doubleInfo, doubleInfo))

    override def term[G: Gradient] = ???
  }

  override def multD = new FEval[scala.Double => scala.Double => scala.Double] {
    override val fec = aInfo(doubleInfo, aInfo(doubleInfo, doubleInfo))

    override def term[G: Gradient] = {
      implicit val gi = implicitly[Gradient[G]].GInfo
      implicit val l = NextLang.apply[LangInfoG, LangTerm, (scala.Double, G)]
      val r = NextLang.apply[LangInfoG, l.repr, (scala.Double, G)]
      l.collapse(r.collapse(r.mkProd__(
        r.multD__(r.rconv(l.zro_(l.in)))(r.zro_(r.in)))(
        r.app(r.app(r.rconv(l.rconv(implicitly[Gradient[G]].plus)))(
          r.app(r.app(r.rconv(l.rconv(implicitly[Gradient[G]].mult)))(r.rconv(l.zro_(l.in))))(r.fst_(r.in))))(
          r.app(r.app(r.rconv(l.rconv(implicitly[Gradient[G]].mult)))(r.zro_(r.in)))(r.rconv(l.fst_(l.in)))))))
    }
  }

  override def expD = new FEval[scala.Double => scala.Double] {
    override val fec = aInfo(doubleInfo, doubleInfo)

    override def term[G: Gradient] = {
      implicit val gi = implicitly[Gradient[G]].GInfo
      implicit val p = NextLang.apply[LangInfoG, LangTerm, (scala.Double, G)]
      p.collapse(p.Let__(p.expD_(p.zro_(p.in)))({
        implicit val ret = NextLang.apply[LangInfoG, p.repr, scala.Double]
        ret.collapse(ret.mkProd__(
          ret.in)(
          ret.app(ret.app(ret.rconv(p.rconv(implicitly[Gradient[G]].mult)))(ret.in))(ret.rconv(p.fst_(p.in)))))
      }))
    }
  }

  override def sigD = new FEval[scala.Double => scala.Double] {
    override val fec = aInfo(doubleInfo, doubleInfo)

    override def term[G: Gradient] = ???
  }

  override def plusD = new FEval[scala.Double => scala.Double => scala.Double] {
    override val fec = aInfo(doubleInfo, aInfo(doubleInfo, doubleInfo))

    override def term[G: Gradient] = {
      implicit val gi = implicitly[Gradient[G]].GInfo
      implicit val l = NextLang.apply[LangInfoG, LangTerm, (scala.Double, G)]
      val r = NextLang.apply[LangInfoG, l.repr, (scala.Double, G)]
      l.collapse(r.collapse(r.mkProd__(
        r.plusD__(r.rconv(l.zro_(l.in)))(r.zro_(r.in)))(
        r.app(r.app(r.rconv(l.rconv(implicitly[Gradient[G]].plus)))(r.rconv(l.fst_(l.in))))(r.fst_(r.in)))))
    }
  }

  override def litD: scala.Double => FEval[scala.Double] = d =>
    new FEval[scala.Double] {
      override val fec = doubleInfo

      override def term[G: Gradient] = base.mkProd__(base.litD(d))(implicitly[Gradient[G]].constG)
    }

  override implicit def doubleInfo: FEvalCase.Aux[scala.Double, Lambda[X => (scala.Double, X)]] =
    new FEvalCase[scala.Double] {
      override type WithGrad[G] = (scala.Double, G)

      override val tm = new FEvalMatch[scala.Double] {
        override type ret = Unit
      }

      override def tmr: tm.ret = ()

      override def wgi[G: Gradient]: LangInfoG[(scala.Double, G)] =
        base.prodInfo(base.doubleInfo, implicitly[Gradient[G]].GInfo)
    }
}

object FEvalDouble extends FEvalDouble